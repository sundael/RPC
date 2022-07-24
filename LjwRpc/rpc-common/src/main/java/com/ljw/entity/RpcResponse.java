package com.ljw.entity;

import com.ljw.emmeration.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  13:38
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {
    private String requestId;
    private Integer statusCode;
    private String message;
    private T data;

    public static <T> RpcResponse<T> success(T data,String requestId){
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setData(data);
        response.setStatusCode(ResponseCode.SUCESS.getCode());
        return response;
    }

    public static <T> RpcResponse<T> fail(String requestId,ResponseCode responseCode){
        RpcResponse<T> response = new RpcResponse<>();
        response.setRequestId(requestId);
        response.setStatusCode(responseCode.getCode());
        response.setMessage(responseCode.getMessage());
        return response;

    }
}
