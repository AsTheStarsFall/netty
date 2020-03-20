package com.study.netty.nio.nio1.channel;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @Desc:FileChannel文件通道
 * @Author: thy
 * @CreateTime: 2018/11/12
 **/

public class NioFileChannel {

    /**
     * 创建文件，向文件中写入数据
     */
    public static void main(String[] args) {

        try {
            //如果文件不存在，创建文件
            File file = new File("D:/nio_utf8.txt");
            if ((!file.exists())) {
                file.createNewFile();
            }
            //通过文件输出流创建与文件相关的通道，从内存写到磁盘
            FileOutputStream fos = new FileOutputStream(file);
            FileChannel channel = fos.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(128);
            byteBuffer.put("Nice to meet you \n".getBytes("UTF-8"));
            byteBuffer.flip();

            //将bytebuffer中position~limit的元素写入通道
            channel.write(byteBuffer);
            byteBuffer.clear();

            byteBuffer.put("拿木剑的温华".getBytes("UTF-8"));
            byteBuffer.flip();

            channel.write(byteBuffer);
            byteBuffer.clear();

            fos.close();
            channel.close();

        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            System.out.println(e);
        }


        /**
         * 从文件中读取字符序列
         */
        try {
            //通过Path创建文件通道
            Path path = Paths.get("D:/nio_utf8.txt");
            FileChannel channel = FileChannel.open(path);

            ByteBuffer byteBuffer = ByteBuffer.allocate((int) (channel.size() + 1));

            Charset utf8 = Charset.forName("UTF-8");
            //阻塞模式，读完才能返回
            channel.read(byteBuffer);
            byteBuffer.flip();

            //将字节转换为字符
            CharBuffer charBuffer = utf8.decode(byteBuffer);
            System.out.println(charBuffer.toString());
            byteBuffer.clear();

            channel.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
