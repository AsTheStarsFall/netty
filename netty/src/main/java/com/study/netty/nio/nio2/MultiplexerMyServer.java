package com.study.netty.nio.nio2;


import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * {@link}
 *
 * @Desc: 服务器
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class MultiplexerMyServer implements Runnable {

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean stop;

    public MultiplexerMyServer(int port) {
        try {
            //打开管道，监听连接
            serverSocketChannel = ServerSocketChannel.open();
            //打开多路复用器
            selector = Selector.open();
            //绑定监听端口
            serverSocketChannel.bind(new InetSocketAddress(port), 1024);
            //设置非阻塞
            serverSocketChannel.configureBlocking(false);
            //注册事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("Time server is start in port :" + port);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void run() {
        while (!stop) {
            try {
                //每隔1s唤醒1次
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> it = selectionKeys.iterator();
                SelectionKey key = null;
                //轮询准备就绪的key
                while (it.hasNext()) {
                    key = it.next();
                    it.remove();
                    try {
                        //处理I/O事件
                        handleInput(key);
                    } catch (Exception e) {
                        if (key != null) {
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

        if (selector != null) {
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()) {
            //处理新接入的请求消息
            if (key.isAcceptable()) {
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking(false);
                //注册
                sc.register(selector, SelectionKey.OP_READ);
                System.out.println("accept from :" + sc.getRemoteAddress());
            }

            if (key.isReadable()) {
                //读取客户端请求消息
                SocketChannel sc = (SocketChannel) key.channel();
                //无法预知客户端发送的码流大小，开辟1MB
                ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                int read = sc.read(readBuffer);
                if (read > 0) {
                    readBuffer.flip();
                    //根据缓冲区可读字节个数，创建字节数组
                    byte[] bytes = new byte[readBuffer.remaining()];
                    //将缓冲区可读字节数组复制到新的字节数组
                    readBuffer.get(bytes);
                    String body = new String(bytes, "UTF-8");
                    System.out.println("Server receives message : " + body);

                    String respStr = StringUtils.isBlank(body) ? "please say something" : "got ur message!";
                    //异步响应消息
                    doWrite(sc, respStr);
                } else if (read < 0) {
                    //链路关闭
                    key.cancel();
                    sc.close();
                } else ;
            }

        }
    }

    private void doWrite(SocketChannel sc, String respStr) throws IOException {
        //字符串编码成字节码数组
        byte[] respStrBytes = respStr.getBytes();
        //开辟缓冲区
        ByteBuffer writeBuffer = ByteBuffer.allocate(respStrBytes.length);
        writeBuffer.put(respStrBytes);
        writeBuffer.flip();
        //不保证一次把需要发送的字节码数组发送完，会出现 写半包的问题
        sc.write(writeBuffer);
    }
}
