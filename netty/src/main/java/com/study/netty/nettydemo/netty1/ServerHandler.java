package com.study.netty.nettydemo.netty1;

import io.netty.channel.*;

import java.net.InetAddress;
import java.util.Date;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2018/11/15
 **/
public class ServerHandler extends SimpleChannelInboundHandler<String> {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n" +
                "it is " + new Date() + "now\r\n");

    }



//    @Override
//    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
//
//        String response;
//        boolean close = false;
//        if (s.isEmpty()) {
//            response = "Please type something";
//        } else if ("bye".equals(s.toLowerCase())) {
//            response = "Bye~";
//            close = true;
//        } else {
//            response = "Did u say'" + s + "' ?/r/n";
//        }
//        ChannelFuture future = channelHandlerContext.write(response);
//        System.out.println(response);
//        if (close) {
//            future.addListener(ChannelFutureListener.CLOSE);
//        }
//    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        String response;
        boolean close = false;
        if (s.isEmpty()) {
            response = "Please type something";
        } else if ("bye".equals(s.toLowerCase())) {
            response = "Bye~";
            close = true;
        } else {
            response = "Did u say'" + s + "' ?/r/n";
        }
        ChannelFuture future = channelHandlerContext.write(response);
        System.out.println(response);
        if (close) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }
}
