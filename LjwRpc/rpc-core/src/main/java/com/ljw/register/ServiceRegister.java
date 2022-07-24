package com.ljw.register;

import java.net.InetSocketAddress;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  16:57
 * @Description: TODO
 * @Version: 1.0
 */
public interface ServiceRegister {
    /**
     *服务注册的接口，servicename注册到这个端口
     * @param serviceName
     * @param inetSocketAddress
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);
}
