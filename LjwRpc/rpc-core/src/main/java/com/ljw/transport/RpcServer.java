package com.ljw.transport;

import com.ljw.serializer.CommonSerializer;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  14:30
 * @Description: TODO
 * @Version: 1.0
 */
public interface RpcServer {
    int DEFAULT_SERIALIZER = CommonSerializer.KRYO_SERIALIZER;
    void start() throws InterruptedException;
    <T> void publishService(T service,String serviceName);
}
