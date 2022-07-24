package com.ljw.handler;

import com.ljw.emmeration.ResponseCode;
import com.ljw.entity.RpcRequest;
import com.ljw.entity.RpcResponse;
import com.ljw.provider.ServiceProvider;
import com.ljw.provider.ServiceProviderImpl;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Slf4j
public class RequestHandler {
    private static final ServiceProvider serviceProvider;
    static {
        serviceProvider=new ServiceProviderImpl();
    }
    public Object handle(RpcRequest rpcRequest){
        Object service = RequestHandler.serviceProvider.getServiceProvider(rpcRequest.getInterfaceName());
        return invokeTargetMethod(rpcRequest,service);
    }

    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service){
        Object result;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
            result=method.invoke(service,rpcRequest.getParamTypes());

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            return RpcResponse.fail( rpcRequest.getRequestId(),ResponseCode.METHODNOTFOUND);
        }
        return result;
    }
}
