package com.study.netty.protocol.http.xml.codec;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.net.InetAddress;
import java.util.List;

/**
 * {@link}
 *
 * @Desc: HTTP+XML HTTP请求消息编码类
 * @Author: thy
 * @CreateTime: 2019/11/27 3:14
 **/
public class HttpXmlRequestEncoder extends AbstractHttpXmlEncoder<HttpXmlRequest> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HttpXmlRequest msg, List<Object> out) throws Exception {
        //xml的字节
        ByteBuf body = encode0(ctx, msg.getBody());
        //构建FullHttpRequest
        FullHttpRequest request = msg.getRequest();
        if (request == null) {
            request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/do", body);
            //消息头
            HttpHeaders headers = request.headers();
            headers.set(HttpHeaders.Names.HOST, InetAddress.getLocalHost().getHostName());
            headers.set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
            headers.set(HttpHeaders.Names.ACCEPT_CHARSET, "ISO-8859-1,utf-8;q=0.7,*;q=0.7");
            headers.set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP.toString() + "," +
                    HttpHeaders.Values.DEFLATE.toString());
            headers.set(HttpHeaders.Names.ACCEPT_LANGUAGE, "zh");
            headers.set(HttpHeaders.Names.USER_AGENT, "Netty http+xml client");
            headers.set(HttpHeaders.Names.ACCEPT,
                    "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        }
        HttpHeaders.setContentLength(request, body.readableBytes());
        //加入编码列表
        out.add(request);
    }
}
