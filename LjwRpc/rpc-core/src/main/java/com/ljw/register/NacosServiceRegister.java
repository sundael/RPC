package com.ljw.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.ljw.emmeration.RpcError;
import com.ljw.exception.RpcException;
import com.ljw.util.NacosUtil;

import java.net.InetSocketAddress;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  17:03
 * @Description: TODO
 * @Version: 1.0
 */
public class NacosServiceRegister implements ServiceRegister {
    @Override
    public void register(String serviceName, InetSocketAddress inetSocketAddress) {
        try {
            NacosUtil.registerService(serviceName,inetSocketAddress);
        } catch (NacosException e) {
            throw new RpcException(RpcError.REGISTER_SERVICE_FAILED);
        }
    }
}
