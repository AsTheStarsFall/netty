package com.study.netty.codec.marshalling;

import com.study.netty.codec.pojo.SubscribeReq;
import io.netty.channel.*;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/26 5:23
 **/
public class MarshallingClientHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Receive server response : [" + msg + "]");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            ctx.write(subReq(i));
        }
        ctx.flush();
    }

    private SubscribeReq subReq(int i) {
        SubscribeReq req = new SubscribeReq();
        req.setAddress("Guangzhou Guangdong");
        req.setPhoneNumber("188xxxxxxxx");
        req.setProductName("Netty Book For Marshalling");
        req.setSubReqID(i);
        req.setUserName("Tianhy");
        return req;
    }
}
