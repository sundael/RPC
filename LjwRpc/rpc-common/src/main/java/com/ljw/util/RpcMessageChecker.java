package com.ljw.util;

import com.ljw.emmeration.ResponseCode;
import com.ljw.emmeration.RpcError;
import com.ljw.entity.RpcRequest;
import com.ljw.entity.RpcResponse;
import com.ljw.exception.RpcException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcMessageChecker {
    public static final String INTERFACE_NAME = "interfaceName";
    private RpcMessageChecker(){

    }
    public static void check(RpcRequest rpcRequest, RpcResponse rpcResponse){
        if (rpcResponse==null) {
            log.error("调用服务失败",rpcRequest.getInterfaceName());
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE,INTERFACE_NAME+":"+rpcRequest.getInterfaceName());
        }
        if (!rpcRequest.getRequestId().equals(rpcResponse.getRequestId())){
            throw new RpcException(RpcError.RESPONSE_NOT_MATCH,INTERFACE_NAME+":"+rpcRequest.getInterfaceName());
        }
        if (rpcResponse.getStatusCode() == null || !rpcResponse.getStatusCode().equals(ResponseCode.SUCESS.getCode())) {
            log.error("调用服务失败,serviceName:{},RpcResponse:{}", rpcRequest.getInterfaceName(), rpcResponse);
            throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, INTERFACE_NAME + ":" + rpcRequest.getInterfaceName());
        }
    }
}
