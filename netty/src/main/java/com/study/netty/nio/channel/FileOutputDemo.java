package com.study.netty.nio.channel;

import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * {@link}
 *
 * @Desc: 写入文件
 * @Author: thy
 * @CreateTime: 2019/8/8 2:12
 **/
public class FileOutputDemo {
    static private final byte message[] = { 83, 111, 109, 101, 32, 98, 121, 116, 101, 115, 46 };

    public static void main(String[] args) {
        try {

            String content = "一串儿字符串";
            FileOutputStream fos = new FileOutputStream("D:\\test.txt");
            FileChannel fc = fos.getChannel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);


            for (int i = 0; i < content.length(); i++) {
                buffer.put(content.getBytes()[i]);
            }
//            for (int i = 0; i < message.length; i++) {
//                buffer.put(message[i]);
//            }
            buffer.flip();
            fc.write(buffer);

            fos.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
