package com.ljw;

import com.ljw.annotation.ServiceScan;
import com.ljw.serializer.CommonSerializer;
import com.ljw.transport.socket.server.SocketServer;

@ServiceScan
public class SocketTestServer {
    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer("127.0.0.1", 5922, CommonSerializer.KRYO_SERIALIZER);
        socketServer.start();
    }
}
