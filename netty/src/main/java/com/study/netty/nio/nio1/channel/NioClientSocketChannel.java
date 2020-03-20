package com.study.netty.nio.nio1.channel;

import com.study.netty.nio.nio1.buffer.Buffers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.nio.charset.Charset;
import java.util.*;

/**
 * @Desc: 客户端每隔1~2秒自动向服务器发送数据，接受服务器返回数据并显示
 * @Author: thy
 * @CreateTime: 2018/11/12
 **/
public class NioClientSocketChannel {

    public static class TCPClient implements Runnable{

        private String threadName;
        private Random random=new Random();

        private InetSocketAddress remoteAddress;

        public TCPClient(String threadName, InetSocketAddress remoteAddress) {
            this.threadName = threadName;
            this.remoteAddress = remoteAddress;
        }

        @Override
        public void run() {

            //解码器
            Charset utf8=Charset.forName("UTF-8");
            Selector selector;

            try {
                //创建TCP通道
                SocketChannel sc=SocketChannel.open();
                sc.configureBlocking(false);
                //创建选择器
                selector = Selector.open();
                //注册感兴趣事件
                int interestSet= SelectionKey.OP_READ | SelectionKey.OP_WRITE;

                //向选择器注册通道
                sc.register(selector,interestSet,new Buffers(256,256));

                //向服务器发起连接，一个通道代表一条TCP连接
                sc.connect(remoteAddress);


                //等待TCP三次握手成功
                while (!sc.finishConnect()){

                }
                System.out.println(threadName+"  " +"finished connection");

            }catch (IOException e){
                System.out.println("client connect failed");
                return;
            }

            // 与服务器断开或线程被中断则中断线程
            try {
                int i=1;
                while (!Thread.currentThread().isInterrupted()){
                    //阻塞等待
                    selector.select();

                    //Set中每个Key代表一个通道
                    Set<SelectionKey> keySet=selector.selectedKeys();
                    Iterator<SelectionKey> it=keySet.iterator();

                    //遍历每个已就需要的通道，处理这个通道已就绪的事件
                    while (it.hasNext()){
                        SelectionKey key=it.next();
                        it.remove();

                        //通过SelectionKey获取对应的通道
                        Buffers buffers= (Buffers) key.attachment();
                        ByteBuffer readBuffer=buffers.getReadBuffer();
                        ByteBuffer writeBuffer=buffers.getWriteBuffer();

                        //通过SelectionKey获取通道对应的缓冲区
                        SocketChannel sc= (SocketChannel) key.channel();

                        //底层socket的读缓冲区有数据可读
                        if(key.isReadable()){
                            sc.read(readBuffer);
                            readBuffer.flip();
                            CharBuffer charBuffer= utf8.decode(readBuffer);
                            System.out.println(charBuffer.array());
                            readBuffer.clear();
                        }
                        //底层socket写缓冲区可写
                        if(key.isWritable()){
                            writeBuffer.put((threadName+" "+i).getBytes("UTF-8"));
                            writeBuffer.flip();

                            //将程序定义的缓冲区的内容写入到socket的缓冲区中
                            sc.write(writeBuffer);
                            writeBuffer.clear();
                            i++;
                        }
                    }
                    Thread.sleep(1000+random.nextInt(1000));

                }


            }catch (InterruptedException e){
                System.out.println(threadName+" is interrupted");
            }catch (IOException e){
                System.out.println("connect err");
            }finally {
                try {
                    selector.close();
                } catch (IOException e) {
                    System.out.println(threadName +"close selector failed");
                }finally {
                    System.out.println(threadName +"closed");
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        InetSocketAddress remoteAddress=new InetSocketAddress("127.0.0.1",8080);
        Thread ta = new Thread(new TCPClient("thread a", remoteAddress));
        Thread tb = new Thread(new TCPClient("thread b", remoteAddress));
        Thread tc = new Thread(new TCPClient("thread c", remoteAddress));
        Thread td = new Thread(new TCPClient("thread d", remoteAddress));
        ta.start();
        tb.start();
        tc.start();

        Thread.sleep(5000);

        /*结束客户端a*/
        ta.interrupt();

        /*开始客户端d*/
        td.start();
    }
}
