package com.study.netty.codec.msgpack;

import com.study.netty.frame.serialize.UserInfo;
import org.msgpack.MessagePack;
import org.msgpack.template.Templates;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * {@link}
 *
 * @Desc: MessagePack Java API Test
 * @Author: thy
 * @CreateTime: 2019/11/11 21:31
 **/
public class MsgPackTest {

    public static void main(String[] args) throws IOException {

        UserInfo userInfo = new UserInfo();

        List<String> list = new ArrayList<>();
        list.add("msgpack");
        list.add("netty");
        list.add("java");
        MessagePack messagePack = new MessagePack();
        //如果序列化自己的类，需要注册
        //messagePack.register(UserInfo.class);

        //serialize
        byte[] bytes = messagePack.write(list);
        //byte[] bytes1 = messagePack.write(userInfo);

        //deserialize
        List<String> read = messagePack.read(bytes, Templates.tList(Templates.TString));

        for (String s : read) {
            System.out.println(s);
        }


    }
}
