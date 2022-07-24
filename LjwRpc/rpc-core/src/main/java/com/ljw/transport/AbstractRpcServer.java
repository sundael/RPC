package com.ljw.transport;

import com.ljw.annotation.Service;
import com.ljw.annotation.ServiceScan;
import com.ljw.emmeration.RpcError;
import com.ljw.exception.RpcException;
import com.ljw.provider.ServiceProvider;
import com.ljw.register.ServiceDiscovery;
import com.ljw.register.ServiceRegister;
import com.ljw.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Set;


/**
 * @Author: ljw
 * @CreateTime: 2022-07-22  16:47
 * @Description: TODO
 * @Version: 1.0
 */
@Slf4j
public abstract class AbstractRpcServer implements RpcServer{
    protected String host;
    protected int port;
    protected ServiceRegister serviceRegister;
    protected ServiceDiscovery serviceDiscovery;
    protected ServiceProvider serviceProvider;

    @Override
    public <T> void publishService(T service, String serviceName) {
        serviceProvider.addServiceProvider(service,serviceName);
        serviceRegister.register(serviceName,new InetSocketAddress(host,port));
    }

    public void scanServices(){
        String mainClassName = ReflectUtil.getStackTrace();
        Class<?> startClass;
        try {
            startClass = Class.forName(mainClassName);
            if (!startClass.isAnnotationPresent(ServiceScan.class)){
                log.error("启动类缺少@ServiceScan注解");
                throw new RpcException(RpcError.SERVICE_SCAN_PACKAGE_NOT_FOUND);
            }
        } catch (ClassNotFoundException e) {
            throw new RpcException(RpcError.UNKNOWN_ERROR);
         }
        String basePackage = startClass.getAnnotation(ServiceScan.class).value();
        if ("".equals(basePackage)){
            basePackage=mainClassName.substring(0,mainClassName.lastIndexOf("."));
        }
        Set<Class<?>> classSet = ReflectUtil.getClasses(basePackage);
        for (Class<?> clazz:classSet){
            if (clazz.isAnnotationPresent(Service.class)){
                String serviceName = clazz.getAnnotation(Service.class).name();
                Object obj;
                try {
                    obj=clazz.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    log.error("创建 " + clazz + " 时有错误发生");
                    continue;
                }
                if ("".equals(serviceName)){
                    Class<?>[] interfaces = clazz.getInterfaces();
                    for (Class<?> ainterface :interfaces){
                        publishService(obj,ainterface.getCanonicalName());
                    }
                }else {
                    publishService(obj,serviceName);
                }
            }
        }



    }


}
