package com.study.netty.bio.bio2;


import java.util.Scanner;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class Bio2Test {

    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bio2Server.start();
            }
        }, "server").start();

        Thread.sleep(1000);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("say something");
                    Scanner scanner = new Scanner(System.in);
                    Bio2Client.send(scanner.next());
                }
            }
        }, "client").start();
    }
}
