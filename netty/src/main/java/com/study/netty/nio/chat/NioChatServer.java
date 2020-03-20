package com.study.netty.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * {@link}
 *
 * @Desc: chat服务器
 * @Author: thy
 * @CreateTime: 2019/8/8 3:28
 **/
public class NioChatServer {
    private int port = 8080;
    private Charset charset = Charset.forName("UTF-8");
    //记录认数以及昵称
    private static HashSet<String> users = new HashSet<>();
    private static String USER_CONTENT_SPILIT = "#@#";
    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";


    private Selector selector = null;

    public NioChatServer(int port) throws Exception {
        this.port = port;

        ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress(this.port));
        server.configureBlocking(false);

        selector = Selector.open();
        //注册
        server.register(selector, SelectionKey.OP_ACCEPT);

        System.out.println("服务器已经启动，端口：" + port);

    }

    public void listen() {
        try {
            while (true) {
                if (selector.select() == 0) {
                    continue;
                }
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    process(key);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private void process(SelectionKey key) throws Exception {
        //连接事件
        if (key.isAcceptable()) {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel sc = ssc.accept();
            sc.configureBlocking(false);
            //注册到SocketChannel,这个连接的数据就由此socketChannel处理
            sc.register(selector, SelectionKey.OP_READ);
            //感兴趣事件设置为accept，接收其他客户端连接
            key.interestOps(SelectionKey.OP_ACCEPT);

            sc.write(charset.encode("请输入昵称"));
        }

        //处理客户端请求数据
        if (key.isReadable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            StringBuilder content = new StringBuilder();
            try {
                while (sc.read(buffer) > 0) {
                    buffer.flip();
                    content.append(charset.decode(buffer));
                }
                key.interestOps(SelectionKey.OP_READ);
            } catch (IOException e) {
                key.cancel();
                if (key.channel() != null) {
                    key.channel().close();
                }
            }

            if (content.length() > 0) {
                String[] contentsArry = content.toString().split(USER_CONTENT_SPILIT);
                //注册用户
                if (contentsArry != null && contentsArry.length == 1) {
                    String nickName = contentsArry[0];
                    if (users.contains(nickName)) {
                        sc.write(charset.encode(USER_EXIST));
                    } else {
                        users.add(nickName);
                        int userCount = onlineCount();
                        String message = "欢迎 【" + nickName + "】来到聊天室,当前在线人数：" + userCount;
                        //广播通知其他客户端
                        broadCast(null, message);
                    }
                }
                else if (contentsArry != null && contentsArry.length > 1) {
                    String nickName = contentsArry[0];
                    String message = content.substring(nickName.length()+ USER_CONTENT_SPILIT.length());
                    message = nickName + "说：" + message;
                    //不回发给发送此消息的客户端
                    if (users.contains(nickName)) {
                        broadCast(sc, message);
                    }
                }
            }
        }
    }

    //在线人数
    private int onlineCount() {
        int i = 0;
        for (SelectionKey key : selector.keys()) {
            Channel c = key.channel();
            if (c instanceof SocketChannel) {
                i++;
            }
        }
        return i;
    }

    //广播
    private void broadCast(SocketChannel sc, String message) throws Exception {
        for (SelectionKey key : selector.keys()) {
            Channel c = key.channel();
            if (c instanceof SocketChannel && c != sc) {
                SocketChannel target = (SocketChannel) c;
                target.write(charset.encode(message));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new NioChatServer(8080).listen();
    }
}
