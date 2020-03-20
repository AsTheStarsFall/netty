package com.study.netty.nettydemo.netty1;

import io.netty.channel.*;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2018/11/15
 **/

/**
 * 多个Handler 可以被多个channel共享
 */
@ChannelHandler.Sharable
public class ClientHandler extends SimpleChannelInboundHandler<String> {

//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//        System.out.println(msg);
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();

    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        System.out.println(s);
    }
}
