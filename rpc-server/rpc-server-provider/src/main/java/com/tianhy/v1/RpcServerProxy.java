package com.tianhy.v1;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link}
 *
 * @Desc: 服务代理类
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class RpcServerProxy {

    ExecutorService es = Executors.newCachedThreadPool();

    public void publish(int port,Object service) {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket accept = serverSocket.accept();
                //数据流的处理交给processHandler处理
                es.execute(new ProcessHandler(accept,service));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
