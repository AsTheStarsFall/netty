package com.study.netty.nio.nio1.buffer;

import java.nio.ByteBuffer;

/**
 * @Desc: 自定义Buffer中读缓冲区和写缓冲区，用于注册通道时的附加对象
 * @Author: thy
 * @CreateTime: 2018/11/12
 **/
public class Buffers {

    ByteBuffer readBuffer;
    ByteBuffer writeBuffer;

    public Buffers(int readCapacity, int writeCapacity) {
        readBuffer=ByteBuffer.allocate(readCapacity);
        writeBuffer=ByteBuffer.allocate(writeCapacity);
    }

    public ByteBuffer getReadBuffer() {
        return readBuffer;
    }

    public ByteBuffer getWriteBuffer() {
        return writeBuffer;
    }
}
