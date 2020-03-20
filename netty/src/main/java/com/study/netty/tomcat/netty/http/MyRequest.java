package com.study.netty.tomcat.netty.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/8/6 7:06
 **/
public class MyRequest {
    private ChannelHandlerContext ctx;
    private HttpRequest request;

    public MyRequest(ChannelHandlerContext ctx, HttpRequest request) {
        this.ctx = ctx;
        this.request = request;
    }

    public String getUri(){
        return request.getUri();
    }

    public String getMethod(){
        return request.getMethod().name();
    }



}
