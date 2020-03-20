package com.study.netty.chat;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/8/10 17:56
 **/
public class MatchTest {
    private Pattern pattern = Pattern.compile("^\\[(.*)\\](\\s\\-\\s(.*))?");
    @Test
    public void match(){
        String str ="[SYSTEM][124343423123][Tom老师] – Student加入聊天室";
        String header = "";
        String content = "";

        Matcher matcher = pattern.matcher(str);
        if(matcher.matches()){
            header = matcher.group(1);
            content = matcher.group(3);
        }

        System.out.println(header);
        System.out.println(content);


    }
}
