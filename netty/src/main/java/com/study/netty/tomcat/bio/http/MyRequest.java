package com.study.netty.tomcat.bio.http;

import java.io.InputStream;

/**
 * {@link}
 *
 * @Desc: 请求封装
 * @Author: thy
 * @CreateTime: 2019/8/6 3:41
 **/
public class MyRequest {

    //GET/POST请求方法
    private String method;
    //请求的路径
    private String url;

    public MyRequest(InputStream is) throws Exception{
        String content = "";
        byte [] buff = new byte[1024];
        int len = 0;
        if((len = is.read(buff)) > 0 ){
            content = new String(buff,0,len);
        }

        /**
         * GET /firstServlet.do HTTP/1.1
         * Host: localhost:8080
         */
//        System.out.println(content);

        //以换行将内容分组
        String line = content.split("\\n")[0];
        // \s 空白字符
        String[] arr = line.split("\\s");
        this.method = arr[0];
        this.url = arr[1].split("\\?")[0];
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }
}
