package com.study.netty.protocol.http.xml.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.List;

/**
 * {@link}
 *
 * @Desc: HTTP+XML HTTP响应编码抽象类
 * @Author: thy
 * @CreateTime: 2019/11/27 5:22
 **/
public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg, List<Object> out) throws Exception {
        //pojo->ByteBuf
        ByteBuf body = encode0(ctx, msg.getBody());
        //构建FullHttpResponse
        FullHttpResponse response = msg.getResponse();
        if (response == null) {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, body);
        } else {
            response = new DefaultFullHttpResponse(msg.getResponse().getProtocolVersion(),
                    msg.getResponse().getStatus(), body);
        }
        //响应头
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/xml");
        HttpHeaders.setContentLength(response, body.readableBytes());
        out.add(response);
    }
}
