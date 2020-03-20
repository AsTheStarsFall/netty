package com.study.netty.nio.buffer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * {@link}
 *
 * @Desc: 直接缓冲区
 * @Author: thy
 * @CreateTime: 2019/8/8 1:08
 **/
public class DirectBuffer {

    public static void main(String[] args) {
        String inFile = "D:\\file.txt";
        try {
            FileInputStream fis = new FileInputStream(inFile);
            //文件操作的FileChannel
            FileChannel fisChannel = fis.getChannel();

            String outFile = "D:\\file_copy.txt";
            FileOutputStream fos = new FileOutputStream(outFile);
            FileChannel fosChannel = fos.getChannel();

            //开辟直接缓冲区，而不是allocate
            ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
            while (true){
                buffer.clear();
                //从已给的channel读到buffer
                int read = fisChannel.read(buffer);
                if(read == -1){
                    break;
                }
                buffer.flip();
                //从已给的Buffer,写入fosChannel
                fosChannel.write(buffer);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
    }
}
