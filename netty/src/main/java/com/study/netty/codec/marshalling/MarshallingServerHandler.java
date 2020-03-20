package com.study.netty.codec.marshalling;

import com.study.netty.codec.pojo.SubscribeReq;
import com.study.netty.codec.pojo.SubscribeResp;
import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/26 5:14
 **/
public class MarshallingServerHandler extends ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        SubscribeReq req = (SubscribeReq) msg;
        if ("Tianhy".equalsIgnoreCase(req.getUserName())) {
            System.out.println("SubReqServer accept client subscribe req :[\r\n" + req.toString() + "]");
            ctx.writeAndFlush(resp(req.getProductName(), req.getSubReqID()));
        }
    }

    private SubscribeResp resp(String productName, int subReqID) {
        SubscribeResp resp = new SubscribeResp();
        resp.setSubReqID(subReqID);
        resp.setDesc("[" + productName + "]" + "ordered succeed!");
        resp.setRespCode(1);
        return resp;
    }
}
