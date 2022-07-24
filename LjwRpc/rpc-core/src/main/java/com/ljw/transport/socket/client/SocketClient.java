package com.ljw.transport.socket.client;

import com.ljw.emmeration.ResponseCode;
import com.ljw.emmeration.RpcError;
import com.ljw.entity.RpcRequest;
import com.ljw.entity.RpcResponse;
import com.ljw.exception.RpcException;
import com.ljw.loadbalancer.LoadBalancer;
import com.ljw.loadbalancer.RandomLoadBalancer;
import com.ljw.register.NacosServiceDiscovery;
import com.ljw.register.ServiceDiscovery;
import com.ljw.serializer.CommonSerializer;
import com.ljw.transport.RpcClient;
import com.ljw.transport.socket.util.ObjectReader;
import com.ljw.transport.socket.util.ObjectWriter;
import com.ljw.util.RpcMessageChecker;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

@Slf4j
public class SocketClient implements RpcClient {
    private final ServiceDiscovery serviceDiscovery;

    private final CommonSerializer serializer;

    public SocketClient(){
        this(DEFAULT_SERIALIZER,new RandomLoadBalancer());
    }
    public SocketClient(LoadBalancer loadBalancer) {
        this(DEFAULT_SERIALIZER, loadBalancer);
    }
    public SocketClient(Integer serializer) {
        this(serializer, new RandomLoadBalancer());
    }

    public SocketClient(Integer serializer, LoadBalancer loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializer);
    }
    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        if(serializer==null){
            log.error("未设置序列化器");
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
        Socket socket = new Socket();
        try {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream,rpcRequest,serializer);
            Object obj = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) obj;
            if (rpcResponse == null) {
                log.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCESS.getCode()) {
                log.error("调用服务失败, service: {}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            RpcMessageChecker.check(rpcRequest,rpcResponse);
            return rpcResponse;
        } catch (IOException e) {
            throw new RpcException("服务调用失败: ", e);
        }
    }
}
