package com.study.netty.frame.tcppackage.correct;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class Netty2ClientHandler1 extends ChannelHandlerAdapter {


    private int count;
    private byte[] req;
//    private final ByteBuf firstMessage;

    public Netty2ClientHandler1() {
        req = ("i am superman!" + System.getProperty("line.separator")).getBytes();
//        firstMessage = Unpooled.buffer(req.length);
//        firstMessage.writeBytes(req);
    }

    //客户端和服务端TCP链路建立成功后，调用此方法，发送消息
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ByteBuf message = null;
        //客户端发送一百次，按照初衷应该收到100条服务端的回应，在未考虑TCP粘包的情况下。count为2，客户端发送消息发生粘包
        for (int i = 0; i < 100; i++) {
            message = Unpooled.buffer(req.length);
            message.writeBytes(req);
            ctx.writeAndFlush(message);
        }
//        ctx.writeAndFlush(firstMessage);
        System.out.println("Send to server message success");
    }

    //支持TCP粘包
    //当服务端回应消息时，调用此方法
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String body = (String) msg;
        System.out.println("Receive server's message :" + body + "; count :" + ++count);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}





