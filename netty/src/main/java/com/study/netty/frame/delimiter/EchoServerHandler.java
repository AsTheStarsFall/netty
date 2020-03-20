package com.study.netty.frame.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/5 7:08
 **/
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelHandlerAdapter {

    int count = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        //由于DelimiterBasedFrameDecoder自动对消息做了解码，后续的ChannelHandler收到的msg是个完整的消息包
        String body = (String) msg;
        System.out.println("This is " + ++count + " times receive client:[" + body + "]");
        //消息尾部拼接分隔符
        body += "$_";
        ByteBuf byteBuf = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(byteBuf);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
