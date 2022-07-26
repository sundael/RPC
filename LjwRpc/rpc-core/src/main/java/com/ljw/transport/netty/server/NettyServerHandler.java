package com.ljw.transport.netty.server;

import com.ljw.entity.RpcRequest;
import com.ljw.entity.RpcResponse;
import com.ljw.factory.SingletonFactory;
import com.ljw.handler.RequestHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-26  09:08
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private final RequestHandler requestHandler;
    public NettyServerHandler() {
        this.requestHandler = SingletonFactory.getInstance(RequestHandler.class);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            if (msg.getHeartBeat()){
                log.info("接收到客户端心跳包....");
                return;
            }
            log.info("服务器接收到请求：{}",msg);
            Object result = requestHandler.handle(msg);
            if (ctx.channel().isActive()&&ctx.channel().isWritable()){
                ctx.writeAndFlush(RpcResponse.success(result, msg.getRequestId()));
            }else {
                log.error("通道不可写");
            }
        }finally {
            ReferenceCountUtil.release(msg);
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleState state = ((IdleStateEvent) evt).state();
            if (state == IdleState.READER_IDLE) {
                log.info("长时间未收到心跳包，断开连接...");
                ctx.close();
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
