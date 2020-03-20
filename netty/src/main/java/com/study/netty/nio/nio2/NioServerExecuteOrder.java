package com.study.netty.nio.nio2;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * {@link}
 *
 * @Desc: NIOServer 逻辑顺序
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class NioServerExecuteOrder {

    public static void main(String[] args) {
        int port = 6666;
        try {
            //1、打开ServerSocketChannel，监听客户端的连接，它是所有客户端连接的父管道
            ServerSocketChannel socketChannel = ServerSocketChannel.open();
            //2、绑定监听端口，设置连接为非阻塞模式
            socketChannel.bind(new InetSocketAddress(InetAddress.getByName("IP"), port));
            socketChannel.configureBlocking(false);
            //3、创建Reactor线程，创建多路复用器Selector并启动线程
            Selector selector = Selector.open();
            new Thread(new ReactorTask()).start();
            //4、将socketChannel注册到reactor线程的多路复用器Selector上，监听ACCEPT事件
            socketChannel.register(selector, SelectionKey.OP_ACCEPT, "ioHandler");
            //5、Selector在无线循环中，轮询准备就绪的Key
            Iterator it = selector.selectedKeys().iterator();
            while (it.hasNext()) {
                SelectionKey key = (SelectionKey) it.next();
                //...处理I/O事件
            }
            //6、Selector监听到有新的客户端连接，处理新的请求，完成TCP三次握手，建立物理连接
            SocketChannel channel = socketChannel.accept();
            //7、设置客户端链路为非阻塞
            channel.configureBlocking(false);
            channel.socket().setReuseAddress(true);
            //8、将新接入的客户端连接注册到Selector上，监听读操作，读取客户端发送的网络消息
            SelectionKey key = socketChannel.register(selector, SelectionKey.OP_READ);
            //9、异步读取客户端请求消息到缓冲区
            int readnumber = (int) channel.read(new ByteBuffer[]{});
            //10、对ByteBuffer编解码，如果有半包消息指针reset，继续读取后续的报文，将解码成功的消息封装成Task
            // 投递到业务线程池中
            //11、将POJO对象encode成ByteBuffer，调用SocketChannel的异步write接口，将消息异步发送给客户端
            channel.write(new ByteBuffer[]{});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ReactorTask implements Runnable {
        @Override
        public void run() {

        }
    }
}
