package com.study.netty.nio.buffer;

import java.nio.ByteBuffer;

/**
 * {@link}
 *
 * @Desc: 手动分配缓冲区，包装数组
 * @Author: thy
 * @CreateTime: 2019/8/8 1:05
 **/
public class BufferWrap {

    public static ByteBuffer defineAllocate(){

        ByteBuffer buffer = ByteBuffer.allocate(128);

        byte[] bytes = new byte[10];
        //包装数组
        ByteBuffer  buffer1= ByteBuffer.wrap(bytes);

        return buffer1;
    }



}
