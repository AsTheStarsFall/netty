package com.study.netty.tomcat.netty.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.apache.commons.lang3.StringUtils;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/8/6 7:14
 **/
public class MyResponse {

    private ChannelHandlerContext ctx;

    public MyResponse(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void write(String out) {

        try {
            if (StringUtils.isBlank(out)) {
                return;
            }
            //设置HTTP请求头信息
            FullHttpResponse fullHttpResponse = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(out.getBytes("UTF-8"))
            );

            fullHttpResponse.headers().set("Content-Type", "text/html");
            ctx.write(fullHttpResponse);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ctx.flush();
            ctx.close();
        }
    }
}
