package com.ljw.register;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.ljw.emmeration.RpcError;
import com.ljw.exception.RpcException;
import com.ljw.loadbalancer.LoadBalancer;
import com.ljw.loadbalancer.RandomLoadBalancer;
import com.ljw.util.NacosUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  17:22
 * @Description: TODO
 * @Version: 1.0
 */
public class NacosServiceDiscovery implements ServiceDiscovery{

    private final LoadBalancer loadBalancer;
    public NacosServiceDiscovery(LoadBalancer loadBalancer){
        if (loadBalancer==null) {
            this.loadBalancer=new RandomLoadBalancer();
        }else {
            this.loadBalancer=loadBalancer;
        }
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            if (instances.size()==0){
                throw new RpcException(RpcError.SERVICE_NOT_FOUND);
            }
            Instance instance = loadBalancer.select(instances);
            return new InetSocketAddress(instance.getIp(),instance.getPort());
        } catch (NacosException e) {
            e.printStackTrace();
        }
        return null;
    }
} 
