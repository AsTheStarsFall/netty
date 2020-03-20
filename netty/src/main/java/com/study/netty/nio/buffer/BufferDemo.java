package com.study.netty.nio.buffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * {@link}
 *
 * @Desc: 读取D://test.txt内容，观察position、limit、capacity变化
 * @Author: thy
 * @CreateTime: 2019/8/8 0:57
 **/
public class BufferDemo {
    public static void main(String[] args) throws IOException {

        FileInputStream fis = new FileInputStream("D:\\test.txt");

        FileChannel fileChannel = fis.getChannel();

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);

        output("初始化", byteBuffer);

        fileChannel.read(byteBuffer);

        output("read()", byteBuffer);

        byteBuffer.flip();
        output("flip()", byteBuffer);

        while (byteBuffer.hasRemaining()) {
            System.out.println((char) byteBuffer.get());
        }

        byteBuffer.clear();
        output("clear()",byteBuffer);

        fileChannel.close();


    }

    //将缓冲区的状态展示
    public static void output(String step, ByteBuffer byteBuffer) {
        System.out.println(step + ":");
        System.out.print("[position：" + byteBuffer.position() + ",");
        System.out.print("limit：" + byteBuffer.limit() + ",");
        //数据只能在position~limit之间

        System.out.println("capacity：" + byteBuffer.capacity() + "]");

    }
}
