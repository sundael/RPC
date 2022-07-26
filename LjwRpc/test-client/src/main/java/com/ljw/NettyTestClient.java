package com.ljw;

import com.ljw.serializer.CommonSerializer;
import com.ljw.transport.RpcClientProxy;
import com.ljw.transport.netty.client.NettyClient;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-26  11:29
 * @Description: TODO
 * @Version: 1.0
 */
public class NettyTestClient {
    public static void main(String[] args) {
        NettyClient client = new NettyClient(CommonSerializer.KRYO_SERIALIZER);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "hello");
        String hello = helloService.Hello(object);
        System.out.println(hello);
        ByeService proxy = rpcClientProxy.getProxy(ByeService.class);
        System.out.println(proxy.bye("netty"));


    }
}
