package com.study.netty.codec.protobuf;

import io.netty.channel.*;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/12 4:53
 **/
public class SubReqServerHandler extends ChannelHandlerAdapter {
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
//    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //将msg转为 SubscribeReqProto.SubscribeReq
        SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
        if ("Tianhy".equalsIgnoreCase(req.getUserName())) {
            System.out.println("SubReqServer accept client subscribe req :[\r\n" + req.toString() + "]");
            ctx.writeAndFlush(resp(req.getProductName(), req.getSubReqID()));
        }
    }

    //响应体
    private SubscribeRespProto.SubscribeResp resp(String productName, int subReqID) {
        SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
        builder.setSubReqID(subReqID);
        builder.setRespCode(1);
        builder.setDesc("[" + productName + "] ordered success!");
        return builder.build();
    }
}
