package com.ljw;

import com.ljw.annotation.ServiceScan;
import com.ljw.serializer.CommonSerializer;
import com.ljw.transport.netty.server.NettyServer;

/**
 * @Author: ljw
 * @CreateTime: 2022-07-26  11:32
 * @Description: TODO
 * @Version: 1.0
 */
@ServiceScan
public class NettyTestServer {
    public static void main(String[] args) {
        NettyServer nettyServer = new NettyServer("127.0.0.1", 9999, CommonSerializer.KRYO_SERIALIZER);
        nettyServer.start();

    }
}
