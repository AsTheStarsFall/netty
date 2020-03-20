package com.study.netty.bio.bio1;

import lombok.extern.java.Log;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Desc: Bio服务端
 * @Author: thy
 * @CreateTime: 2018/11/11
 **/

@Log
public class BioServer {
    /**
     * 默认端口号
     */
    private static int DEFAULT_PORT = 7777;

    /**
     * 单例的ServerSocket
     */
    private static ServerSocket serverSocket;

    //根据传入参数设置监听端口，如果没有传入，则使用默认值
    public static void start() throws IOException {
        start(DEFAULT_PORT);
    }

    public synchronized static void start(int port) throws IOException {
        if (serverSocket != null) return;

        try {
            serverSocket = new ServerSocket(port);
            log.info("服务端已启动，端口号：" + port);

            //通过无限循环监听客户端连接，如果没有客户端连接，将阻塞在accept操作
            //阻塞式等待客户端连接，有连接才返回socket对象
            while (true){
                Socket socket = serverSocket.accept();
                new Thread(new BioServerHandler(socket)).start();
            }

        } finally {
            if(serverSocket != null){
                log.info("服务端已关闭");
                serverSocket.close();
                serverSocket = null;
            }
        }
    }

}
