package com.ljw.transport.netty.client;

import com.ljw.entity.RpcResponse;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-25  16:49
 * @Description: TODO
 * @Version: 1.0
 */
public class UnprocessedRequests {
    private static ConcurrentHashMap<String, CompletableFuture<RpcResponse>>  unprocessedResponseFutures =  new ConcurrentHashMap<>();
    public void put(String requestId,CompletableFuture<RpcResponse> future){
        unprocessedResponseFutures.put(requestId,future);
    }
    public void remove(String requestId){
        unprocessedResponseFutures.remove(requestId);
    }
    public void complete(RpcResponse rpcResponse){
        CompletableFuture<RpcResponse> future = unprocessedResponseFutures.remove(rpcResponse.getRequestId());
        if (null!=future){
            future.complete(rpcResponse);
        }else {
            throw new IllegalStateException();
        }
    }
}
