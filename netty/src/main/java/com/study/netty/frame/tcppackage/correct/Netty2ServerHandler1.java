package com.study.netty.frame.tcppackage.correct;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * {@link}
 *
 * @Desc: I/O事件处理类
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class Netty2ServerHandler1 extends ChannelHandlerAdapter {

    private int count;
//    private String firstResp;

    //支持TCP粘包
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("Server receive message :" + body + "; count :" + ++ count );

        String respStr = "got ur message!" + System.getProperty("line.separator");

        ByteBuf resp = Unpooled.copiedBuffer(respStr.getBytes());

        //异步发送客户端
        //不是直接写入通道中，把待发送的消息放到缓冲数组中
        ctx.writeAndFlush(resp);
    }

//    @Override
//    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
//        firstResp = "Welcome to " + InetAddress.getLocalHost().getHostName() + " it is " + new Date() + "now";
//        ByteBuf resp = Unpooled.copiedBuffer(firstResp.getBytes());
//        ctx.writeAndFlush(resp);
//    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将消息队列中的消息写入SocketChannel中发送对方
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

}
