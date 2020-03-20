package com.study.netty.bio.bio2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class Bio2Server {

    private static final int PORT = 6666;
    protected static ServerSocket serverSocket;

    public static void start() {
        start(PORT);
    }

    private static synchronized void start(int port) {
        if (serverSocket != null) return;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is started in port " + port);
            Socket socket = null;
            while (true) {
                socket = serverSocket.accept();
                //当有新的客户端接入的时候，创建一个新的客户端线程处理这条socket链路
                new Thread(new Bio2ServerHandler(socket)).start();
            }
        } catch (Exception e) {

        } finally {
            if (serverSocket != null) {
                System.out.println("Server will be closed");
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                serverSocket = null;
            }
        }

    }


}
