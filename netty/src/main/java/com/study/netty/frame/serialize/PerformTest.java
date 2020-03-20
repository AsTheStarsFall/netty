package com.study.netty.frame.serialize;

import org.msgpack.MessagePack;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * {@link}
 *
 * @Desc: 性能测试, 对Java序列化和二进制编码分别进行 100W 次编码
 * @Author: thy
 * @CreateTime: 2019/11/6 19:43
 **/
public class PerformTest {
    public static void main(String[] args) throws IOException {

        UserInfo userInfo = new UserInfo();
        userInfo.createUserId(93).createUserName("thy");
        int loop = 1000000;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream os = null;
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < loop; i++) {
            bos = new ByteArrayOutputStream();
            os = new ObjectOutputStream(bos);
            os.writeObject(userInfo);
            os.flush();
            os.close();
            byte[] bytes = bos.toByteArray();
            bos.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("The JDK Serializable costs time is :" + (endTime - startTime) + "ms");

        startTime = System.currentTimeMillis();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        for (int i = 0; i < loop; i++) {
            byte[] bytes = userInfo.codec(byteBuffer);
        }
        endTime = System.currentTimeMillis();
        System.out.println("The byte array serializable costs time is:" + (endTime - startTime) + "ms");

        //MessagePack
        startTime = System.currentTimeMillis();
        MessagePack messagePack = new MessagePack();
        //注册要序列化的类型
        messagePack.register(UserInfo.class);
        for (int i = 0; i < loop; i++) {
            byte[] write = messagePack.write(userInfo);
        }
        endTime = System.currentTimeMillis();
        System.out.println("The MessagePack serializable costs time is:" + (endTime - startTime) + "ms");

        //The JDK Serializable costs time is :3289ms
        //The byte array serializable costs time is:166ms
        //The MessagePack serializable costs time is:2282ms

    }
}
