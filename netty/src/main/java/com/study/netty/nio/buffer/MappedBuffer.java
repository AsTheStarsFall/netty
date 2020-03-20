package com.study.netty.nio.buffer;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * {@link}
 *
 * @Desc: 内存映射
 * @Author: thy
 * @CreateTime: 2019/8/8 2:00
 **/
public class MappedBuffer {

    public static void main(String[] args) {
        try {
            RandomAccessFile raf = new RandomAccessFile("D:\\file.txt", "rw");
            long fileSize = raf.length();
            FileChannel fc = raf.getChannel();
            byte[] bytes = new byte[(int) fileSize];
            //将缓冲区与文件系统进行关联
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);

            //对缓冲区数据进行操作，文件内容也会发生变化
/*            mbb.put(0, (byte) 97);  //a
            mbb.put(25, (byte) 122);   //z*/
/*            while (mbb.hasRemaining()){
                System.out.println(mbb.get());
            }*/
            for (int i = 0; i < fileSize; i++) {
                bytes[i] = mbb.get();
            }
            System.out.println(new String(bytes, Charset.defaultCharset()));
            raf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
