package com.study.netty.nio.nio2;


/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class MyServer {

    public static void main(String[] args) {
        int port = 6666;
        MultiplexerMyServer server = new MultiplexerMyServer(port);
        new Thread(server, "Nio-server-001").start();
    }
}
