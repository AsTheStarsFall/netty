package com.study.netty.protocol.http.xml.client;

import com.study.netty.protocol.http.xml.codec.HttpXmlRequest;
import com.study.netty.protocol.http.xml.codec.HttpXmlResponse;
import com.study.netty.protocol.http.xml.pojo.OrderFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/27 9:21
 **/
public class HttpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //链路建立成功以后，创建一个订单，封装成HttpXmlRequest（其中body为order），编码后传输
        HttpXmlRequest request = new HttpXmlRequest(null, OrderFactory.create(123));
        ctx.writeAndFlush(request);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
        System.out.println("HttpXmlClient receive response of http headers :"
                + msg.getResponse().headers().names());
        System.out.println("HttpXmlClient receive response of http body :"
                + msg.getBody().toString());

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
