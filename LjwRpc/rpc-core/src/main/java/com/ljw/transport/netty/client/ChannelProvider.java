package com.ljw.transport.netty.client;

import com.ljw.codec.CommonDecoder;
import com.ljw.codec.CommonEncoder;
import com.ljw.serializer.CommonSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-25  17:05
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
public class ChannelProvider {
    private static EventLoopGroup eventLoopGroup;
    private static Bootstrap bootstrap = initializeBootstrap();

    private static Map<String, Channel> channels = new ConcurrentHashMap<>();

    public static Channel get(InetSocketAddress inetSocketAddress, CommonSerializer serializer ){
        String key = inetSocketAddress.toString() + serializer.getCode();
        if (channels.containsKey(key)){
            Channel channel = channels.get(key);
            if (channel!=null&&channel.isActive()){
                return channel;
            }else {
                channels.remove(key);
            }
        }
        bootstrap.handler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new CommonEncoder(serializer))
                                .addLast(new IdleStateHandler(0,5,0, TimeUnit.SECONDS))
                                .addLast(new CommonDecoder())
                                .addLast(new NettyClentHandler());
                    }
                }
        );
        Channel channel = null;
        try {
            channel = connect(bootstrap, inetSocketAddress);
        } catch (ExecutionException e) {
            log.error("连接客户端时有错误发生", e);
            return null;
        }
        channels.put(key, channel);
        return channel;
    }

    private static Channel connect(Bootstrap bootstrap, InetSocketAddress inetSocketAddress) throws ExecutionException, InterruptedException {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future->{
            if (future.isSuccess()){
                log.info("客户端连接成功");
                completableFuture.complete(future.channel());
            }else {
                throw new IllegalStateException();
            }
                }
                );
        return completableFuture.get();

    }

    private static Bootstrap initializeBootstrap(){
        eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,5000)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true);
        return bootstrap;
    }
}
