package com.study.netty.frame.serialize;

import org.msgpack.MessagePack;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/6 19:17
 **/
public class Test {

    public static void main(String[] args) throws IOException {
        UserInfo userInfo = new UserInfo();
        userInfo.createUserId(93).createUserName("thy");
        //JDK序列化机制编码
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(bos);

        os.writeObject(userInfo);
        os.flush();
        os.close();

        //编码后的字节数组
        byte[] b = bos.toByteArray();
        System.out.println("JDK serializable length is :" + b.length);
        bos.close();
        System.out.println("The byte array serializable length is :" + userInfo.codec().length);

        //MessagePack
        MessagePack msgpack = new MessagePack();
        //注册要序列化的类型
        msgpack.register(UserInfo.class);
        byte[] write = msgpack.write(userInfo);
        System.out.println("The MessagePack serializable length is :" + write.length);

        //JDK serializable length is :112
        //The byte array serializable length is :11
        //The MessagePack serializable length is :6
    }
}
