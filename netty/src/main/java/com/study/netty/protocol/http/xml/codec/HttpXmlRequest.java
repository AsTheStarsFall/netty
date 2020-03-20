package com.study.netty.protocol.http.xml.codec;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.Data;

/**
 * {@link}
 *
 * @Desc: HTTP+XML http请求消息体
 * @Author: thy
 * @CreateTime: 2019/11/27 3:22
 **/
@Data
public class HttpXmlRequest {

    private FullHttpRequest request;
    private Object body;

    public HttpXmlRequest(FullHttpRequest request, Object body) {
        this.request = request;
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpXmlRequest{" +
                "request=" + request +
                ", body=" + body +
                '}';
    }
}
