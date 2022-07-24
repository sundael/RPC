package com.ljw.transport.socket.server;

import com.ljw.factory.ThreadPoolFactory;
import com.ljw.handler.RequestHandler;
import com.ljw.hook.ShutdownHook;
import com.ljw.provider.ServiceProviderImpl;
import com.ljw.register.NacosServiceRegister;
import com.ljw.serializer.CommonSerializer;
import com.ljw.transport.AbstractRpcServer;
import com.ljw.transport.socket.client.SocketClient;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;

@Slf4j
public class SocketServer extends AbstractRpcServer {
    private final ExecutorService threadPool;
    private final CommonSerializer serializer;
    private final RequestHandler requestHandler = new RequestHandler();

    public SocketServer(String host, int port){
        this(host,port,DEFAULT_SERIALIZER);
    }
    public SocketServer(String host,int port,Integer serializer){
        this.host=host;
        this.port=port;
        threadPool = ThreadPoolFactory.createDefaultThreadPool("socket-rpc-server");
        this.serviceRegister = new NacosServiceRegister();
        this.serviceProvider = new ServiceProviderImpl();
        this.serializer = CommonSerializer.getByCode(serializer);
        scanServices();
    }

    @Override
    public void start() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(host,port));
            ShutdownHook.getShutdownHook().addClearAllHook();
            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                log.info("消费者连接: {}:{}", socket.getInetAddress(), socket.getPort());
                threadPool.execute(new SocketRequestHandlerThread(socket, requestHandler, serializer));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
