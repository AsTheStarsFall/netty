package com.study.netty.nio.nio1.channel;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2018/11/12
 **/

import com.study.netty.nio.nio1.buffer.Buffers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * 服务端：接收客户端消息并显示
 */
public class NioServerSocketChannel {

    public static class TCPServer implements Runnable {

        //服务器地址
        InetSocketAddress localAddress;

        public TCPServer(int port) {
            this.localAddress = new InetSocketAddress(port);
        }

        @Override
        public void run() {
            Charset utf8 = Charset.forName("UTF-8");

            ServerSocketChannel ssc = null;
            Selector selector = null;

            Random random = new Random();

            try {
                //创建选择器
                selector=Selector.open();
                //创建服务器通道
                ssc= ServerSocketChannel.open();
                ssc.configureBlocking(false);

                //设置监听服务器端口，这是最大连接缓冲为100
                ssc.bind(localAddress,100);
                //channel注册selector
                ssc.register(selector, SelectionKey.OP_ACCEPT);


            } catch (IOException e) {
                System.out.println("Server started failed!");
                return;
            }
            System.out.println("Server started with address:"+localAddress);

            //服务器线程被中断后会退出
            try {
                while (!Thread.currentThread().isInterrupted()){
                    int s=selector.select();
                    if(s==0){
                        continue;
                    }

                    Set<SelectionKey> keySet=selector.selectedKeys();
                    Iterator<SelectionKey> it=keySet.iterator();
                    SelectionKey key=null;
                    while (it.hasNext()){
                        key=it.next();
                        it.remove();

                        //如果发现异常，说明客户端连接出现问题，服务器正常运行
                        try {
                            //ssc通道只对连接事件感兴趣
                            if(key.isAcceptable()){
                                //accept方法会返回一个普通的通道，每个通道在内核中都对应一个socket缓冲区
                                SocketChannel sc=ssc.accept();
                                sc.configureBlocking(false);

                                //向选择器注册这个通道和普通通道感兴趣事件，同时提供这个通道相关的缓冲区
                                int interestSet=SelectionKey.OP_READ;
                                sc.register(selector,interestSet,new Buffers(256,256));

                                System.out.println("accept from"+ sc.getRemoteAddress());
                            }

                            // 普通 通道感兴趣读事件且有数据可读
                            if(key.isReadable()){
                                //通过SelectionKey获取通道对应的缓冲区
                                Buffers buffers= (Buffers) key.attachment();
                                ByteBuffer readBufer=buffers.getReadBuffer();
                                ByteBuffer writeBuffer=buffers.getWriteBuffer();

                                //通过SelectionKey获取对应的通道
                                SocketChannel sc= (SocketChannel) key.channel();

                                //从底层socket读缓冲区中读数据
                                sc.read(readBufer);
                                readBufer.flip();

                                //解码客户端发来的消息
                                CharBuffer charBuffer=utf8.decode(readBufer);
                                System.out.println(charBuffer.array());
                                //从position开始到limit可读,这个时候的limit是不动的，在结尾处。如果要重复读的话，提高并发性能，一边读一边写。
                                readBufer.rewind();


                                //准备好向客户端发送的消息
                                writeBuffer.put("from server:".getBytes("utf-8"));
                                writeBuffer.put(readBufer);

                                readBufer.clear();

                                //设置通道事件
                                key.interestOps(key.interestOps() |SelectionKey.OP_WRITE);

                            }

                            //通道感兴趣写事件
                            if(key.isWritable()){
                                Buffers buffers= (Buffers) key.attachment();
                                ByteBuffer writeBuffer=buffers.getWriteBuffer();
                                writeBuffer.flip();

                                SocketChannel sc= (SocketChannel) key.channel();
                                int len=0;
                                while (writeBuffer.hasRemaining()){
                                    len=sc.write(writeBuffer);
                                    //底层socket写缓冲已满
                                    if(len==0){
                                        break;
                                    }
                                }
                                writeBuffer.compact();
                                //数据全部写入到底层socket写缓冲区
                                if(len!=0){
                                    //取消通道写的事件
                                    key.interestOps(key.interestOps() & (~SelectionKey.OP_WRITE));
                                }
                            }
                        }catch (IOException e){
                            System.out.println("client err");
                            //如果客户端连接异常，从Selector移除这个key
                            key.cancel();
                            key.channel().close();
                        }

                    }
                    Thread.sleep(random.nextInt(500));
                }


            }catch (InterruptedException e){
                System.out.println("ServerThread is interrupted");
            }catch (IOException e){
                System.out.println("ServerThread selector err");
            }finally {
                try {
                    selector.close();
                } catch (IOException e) {
                    System.out.println("selector closed failed");
                }finally {
                    System.out.println("Server closed");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread=new Thread(new TCPServer(8080));
        thread.start();
        Thread.sleep(100000);
        thread.interrupt();
    }

}
