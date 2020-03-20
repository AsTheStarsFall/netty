package com.study.netty.nio.chat;

import org.apache.commons.lang3.StringUtils;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * {@link}
 *
 * @Desc: NioChat客户端
 * @Author: thy
 * @CreateTime: 2019/8/8 5:07
 **/
public class NioChatClient {

    private final InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost",8080);
    Selector selector = null;
    SocketChannel sc = null;
    private String nickName = "";
    private Charset charset = Charset.forName("UTF-8");
    private static String USER_EXIST = "系统提示：该昵称已经存在，请换一个昵称";
    private static String USER_CONTENT_SPILIT = "#@#";


    public NioChatClient() throws Exception{
        selector = Selector.open();
        sc = SocketChannel.open(inetSocketAddress);
        sc.configureBlocking(false);
        sc.register(selector, SelectionKey.OP_READ);
    }

    public void sesssion(){
        //读线程
        new Read().start();
        //写线程
        new Write().start();
    }

    private class Write extends Thread{
        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNext()){
                    String line = scanner.nextLine();
                    if(StringUtils.isBlank(line)){continue;}
                    if(StringUtils.isBlank(nickName)){
                        nickName = line;
                        line = nickName + USER_CONTENT_SPILIT;
                    }else{
                        line = nickName + USER_CONTENT_SPILIT + line;
                    }
                    sc.write(charset.encode(line));
                }
                scanner.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class Read extends Thread{
        @Override
        public void run() {
            try {
                while (true){
                    int select = selector.select();
                    if(select == 0){continue;}
                    //获取通道集合
                    Set<SelectionKey> keys = selector.selectedKeys();
                    Iterator<SelectionKey> iterator = keys.iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        iterator.remove();
                        process(key);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        private void process(SelectionKey key)throws Exception{
            if(key.isReadable()) {
                SocketChannel sc = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                String content = "";
                while (sc.read(buffer) > 0) {
                    buffer.flip();
                    content += charset.decode(buffer);
                }
                if (USER_EXIST.equals(content)) {
                    nickName = "";
                }
                System.out.println(content);
                key.interestOps(SelectionKey.OP_READ);

            }
        }
    }

    public static void main(String[] args) throws Exception{
        new NioChatClient().sesssion();
    }
}
