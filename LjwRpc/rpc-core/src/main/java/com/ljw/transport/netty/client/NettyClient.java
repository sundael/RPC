package com.ljw.transport.netty.client;

import com.ljw.emmeration.RpcError;
import com.ljw.entity.RpcRequest;
import com.ljw.entity.RpcResponse;
import com.ljw.exception.RpcException;
import com.ljw.factory.SingletonFactory;
import com.ljw.loadbalancer.LoadBalancer;
import com.ljw.loadbalancer.RandomLoadBalancer;
import com.ljw.register.NacosServiceDiscovery;
import com.ljw.register.ServiceDiscovery;
import com.ljw.serializer.CommonSerializer;
import com.ljw.transport.RpcClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-25  16:46
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
public class NettyClient implements RpcClient {
    private static final EventLoopGroup group;
    private static final Bootstrap bootstrap;
    static {
        group=new NioEventLoopGroup();
        bootstrap =new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class);
    }
    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;

    private final UnprocessedRequests unprocessedRequests;

    public NettyClient() {
        this(DEFAULT_SERIALIZER, new RandomLoadBalancer());
    }
    public NettyClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }
    public NettyClient(Integer serializer) {
        this(serializer, new RandomLoadBalancer());
    }
    public NettyClient(Integer serializer, LoadBalancer loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializer);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }
    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());

    }
}
