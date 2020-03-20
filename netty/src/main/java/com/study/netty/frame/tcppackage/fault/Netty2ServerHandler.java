package com.study.netty.frame.tcppackage.fault;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetAddress;
import java.util.Date;

/**
 * {@link}
 *
 * @Desc: I/O事件处理类
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class Netty2ServerHandler extends ChannelHandlerAdapter {

    private int count;
    private String firstResp;

    //未支持TCP粘包
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        //根据缓冲区可读字节数创建字节数组
        byte[] req = new byte[buf.readableBytes()];
        //将缓冲区的字节数组复制到新建的字节数组中
        buf.readBytes(req);
        //构建请求消息体
        String body = new String(req, "UTF-8");
        //客户端发送了100条，count应当为100，未考虑TCP粘包的情况下，count为1，服务端响应消息发生粘包
        System.out.println("Server receive message :" + body + "; count :" + ++ count );

        String respStr = "got ur message!";

        ByteBuf resp = Unpooled.copiedBuffer(respStr.getBytes());

        //异步发送客户端
        //不是直接写入通道中，把待发送的消息放到缓冲数组中
        ctx.write(resp);
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        firstResp = "Welcome to " + InetAddress.getLocalHost().getHostName() + " it is " + new Date() + "now";
        ByteBuf resp = Unpooled.copiedBuffer(firstResp.getBytes());
        ctx.writeAndFlush(resp);
    }

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
