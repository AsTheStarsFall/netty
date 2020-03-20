package com.study.netty.nio.nio1.buffer;

import lombok.extern.java.Log;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2018/11/12
 **/
@Log
public class BufferDemo {

    // 解码
    public static void decode(String str) throws UnsupportedEncodingException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
        byteBuffer.put(str.getBytes("UTF-8"));
        byteBuffer.flip(); //从position开始到limit可读

        //获取utf8的解码器
        Charset utf8 = Charset.forName("UTF-8");
        //对bytebuffer中的内容进行解码
        CharBuffer charBuffer = utf8.decode(byteBuffer);

        //array()返回的就是内部的数据引用，编码以后的有效长度为0-limit
        char[] charArr = Arrays.copyOf(charBuffer.array(), charBuffer.limit());
        System.out.println("解码:");
        System.out.println(charArr);
    }

    //编码
    public static void encode(String str) throws UnsupportedEncodingException {
        CharBuffer charBuffer = CharBuffer.allocate(128);
        charBuffer.append(str);
        charBuffer.flip();

        //获取utf8的解码器
        Charset utf8 = Charset.forName("UTF-8");
        //对charBuffer中的内容进行编码
        ByteBuffer byteBuffer=utf8.encode(charBuffer);

        //array()返回的就是内部的数据引用，编码以后的有效长度为0-limit
        byte[] bytes=Arrays.copyOf(byteBuffer.array(),byteBuffer.limit());
        System.out.println("编码："+Arrays.toString(bytes));


    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        decode("温华");
        encode("温华");
    }
}
