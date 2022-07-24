package com.ljw.transport;

import com.ljw.entity.RpcRequest;
import com.ljw.serializer.CommonSerializer;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  14:29
 * @Description: TODO
 * @Version: 1.0
 */
public interface RpcClient {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;
    Object sendRequest(RpcRequest rpcRequest);
}
