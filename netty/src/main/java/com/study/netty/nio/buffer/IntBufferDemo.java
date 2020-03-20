package com.study.netty.nio.buffer;

import java.nio.IntBuffer;

/**
 * {@link}
 *
 * @Desc: int类型的缓冲区
 * @Author: thy
 * @CreateTime: 2019/11/5 17:03
 **/
public class IntBufferDemo {

    public static void main(String[] args) {
        //开辟缓冲空间
        //新缓冲区的当前位置为零，limit是其容量
        IntBuffer intBuffer = IntBuffer.allocate(8);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            //position递增
            intBuffer.put(i);
        }
        //将position当前位置设为0，limit设为当前位置，position~limit可读
        intBuffer.flip();
        //如果position~limit有元素
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
