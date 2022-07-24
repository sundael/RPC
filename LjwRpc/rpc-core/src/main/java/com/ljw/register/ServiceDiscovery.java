package com.ljw.register;

import java.net.InetSocketAddress;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  17:02
 * @Description: TODO
 * @Version: 1.0
 */
public interface ServiceDiscovery {
    InetSocketAddress lookupService(String serviceName);
}
