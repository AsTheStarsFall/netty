package com.study.netty.nio.buffer;

import java.nio.ByteBuffer;

/**
 * {@link}
 *
 * @Desc: 缓冲区分片
 * @Author: thy
 * @CreateTime: 2019/8/8 0:58
 **/
public class BufferSlice {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        for (int i = 0; i < byteBuffer.capacity(); i++) {
            byteBuffer.put((byte) i);
        }
        byteBuffer.position(3);
        byteBuffer.limit(7);
        //创建子缓冲区position~limit/3~7区间[3,4,5,6]
        ByteBuffer slice = byteBuffer.slice();
        //改变子缓冲区内容
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 10;
            slice.put(i,b);
        }
        //恢复position、limit
        byteBuffer.position(0);
        byteBuffer.limit(byteBuffer.capacity());

        while (byteBuffer.remaining()>0){
            System.out.println(byteBuffer.get());
        }
    }
}
