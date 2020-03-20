package com.study.netty.tomcat.bio.http;

import java.io.OutputStream;

/**
 * {@link}
 *
 * @Desc: 响应封装
 * @Author: thy
 * @CreateTime: 2019/8/6 3:41
 **/
public class MyResponse {

    private OutputStream os;

    public MyResponse(OutputStream os) {
        this.os = os;
    }

    public void write(String s) throws Exception{
        //输出遵循HTTP协议
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\n")
                .append("Content-Type: text/html;\n")
                .append("\r\n")
                .append(s);

        os.write(sb.toString().getBytes());
    }
}

