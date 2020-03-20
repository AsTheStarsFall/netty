package com.study.netty.nio.nio2;

/**
 * {@link}
 *
 * @Desc: 客户端
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class MyClient {

    public static void main(String[] args) {
        int port = 6666;
        MyClientHandler client = new MyClientHandler("127.0.0.1", port);
        new Thread(client, "Nio-client-001").start();
    }
}
