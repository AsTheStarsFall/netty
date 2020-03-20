package com.study.netty.bio.bio1;


import java.io.IOException;
import java.util.Scanner;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2018/11/11
 **/
public class BioTest {


    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BioServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

        //防止客户端先于服务端启动
        Thread.sleep(100);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println(("输入内容..."));
                    Scanner scanner = new Scanner(System.in);
                    BioClient.send(scanner.next());
                }
            }
        }).start();
    }
}
