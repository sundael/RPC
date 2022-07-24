package com.ljw.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  13:35
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RpcRequest implements Serializable {

    private String requestId;
    /**
     * 调用的接口，方法名字
     */
    private String interfaceName;
    private String methodName;
    /**
     *调用方法参数
     */
    private Object[] parameters;
    private Class<?>[] paramTypes;


}
