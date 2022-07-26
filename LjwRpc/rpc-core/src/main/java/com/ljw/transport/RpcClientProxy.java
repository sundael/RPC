package com.ljw.transport;

import com.ljw.entity.RpcRequest;
import com.ljw.entity.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  14:35
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private final  RpcClient Client;

    public RpcClientProxy(RpcClient rpcClient) {
        this.Client = rpcClient;
    }

    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest=new RpcRequest(UUID.randomUUID().toString(),method.getDeclaringClass().getName(),
                method.getName(),args,method.getParameterTypes(),false);
        RpcResponse rpcResponse =null;
        rpcResponse = (RpcResponse) Client.sendRequest(rpcRequest);
        return rpcResponse.getData();
    }
}
