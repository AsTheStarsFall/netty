package com.study.netty.nettydemo.netty1;

import java.io.*;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/3/19
 **/
public class Test {

    public static void main(String[] args) throws IOException {
        BufferedReader in=new BufferedReader(new InputStreamReader(System.in));
        for(;;){
            String line = in.readLine();
            if(line == null){
                break;
            }
            System.out.println(line);
        }


    }
}
