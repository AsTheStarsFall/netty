package com.study.netty.nio.buffer;

import java.nio.ByteBuffer;

/**
 * {@link}
 *
 * @Desc: 只读缓冲区
 * @Author: thy
 * @CreateTime: 2019/11/5 17:49
 **/
public class ReadOnlyBuffer {
    public static void main(String[] args) {
        ByteBuffer original = ByteBuffer.allocate(10);
        for (int i = 0; i < original.capacity(); i++) {
            original.put((byte) i);
        }
        //创建只读缓冲区
        ByteBuffer readOnlyBuffer = original.asReadOnlyBuffer();

        //改变原生缓冲区内容，通过分片改变3~7区间的值
        original.position(3);
        original.limit(7);

        //创建分片
        ByteBuffer slice = original.slice();
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 10;
            slice.put(i,b);
        }
        //恢复原缓冲区
        original.position(0);
        original.limit(original.capacity());

        readOnlyBuffer.position(0);
        readOnlyBuffer.limit(original.capacity());

        //只读缓冲区也随之改变
        while (readOnlyBuffer.hasRemaining()){
            System.out.println(readOnlyBuffer.get());
        }





    }
}
