package com.study.netty.nettydemo.netty0;

import io.netty.channel.*;
import lombok.extern.java.Log;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2018/11/15
 **/
@Log
public class MyClient extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       log.info("client receive message :"+ msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("client err");
    }
}
