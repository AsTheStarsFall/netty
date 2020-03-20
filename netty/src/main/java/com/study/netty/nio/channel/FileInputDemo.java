package com.study.netty.nio.channel;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * {@link}
 *
 * @Desc: 读取文件
 * @Author: thy
 * @CreateTime: 2019/8/8 2:09
 **/
public class FileInputDemo {

    public static void main(String[] args) {
        try {
            FileInputStream fis = new FileInputStream("D:\\file.txt");
            //获取通道
            FileChannel fc = fis.getChannel();
            //开辟缓冲空间
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            //读到缓冲区
            fc.read(buffer);

            //从position到limit可读
            buffer.flip();

            while (buffer.remaining()>0){
                byte b = buffer.get();
                System.out.println((char)b);
            }

            fis.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
