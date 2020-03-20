package com.study.netty.tomcat;


import com.study.netty.tomcat.bio.BioTomcat;
import com.study.netty.tomcat.netty.NettyTomcat;

/**
 * {@link}
 *
 * @Desc: Tomcat启动类
 * @Author: thy
 * @CreateTime: 2019/8/6
 **/
public class Main {

    public static void main(String[] args) {

        new BioTomcat().start();

//        new NettyTomcat().start();
    }
}
