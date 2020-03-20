package com.study.netty.nettydemo.netty0;

import io.netty.channel.*;
import lombok.extern.java.Log;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2018/11/15
 **/
@Log
public class TCPServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("server alive");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
       log.info("server receive msg: "+ msg);
        ctx.channel().writeAndFlush("accept msg："+msg);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warning("server exception:"+cause.getMessage());
    }
}
