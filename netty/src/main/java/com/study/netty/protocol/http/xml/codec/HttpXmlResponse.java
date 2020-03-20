package com.study.netty.protocol.http.xml.codec;

import io.netty.handler.codec.http.FullHttpResponse;
import lombok.Data;

/**
 * {@link}
 *
 * @Desc: HTTP+XML http响应消息体
 * @Author: thy
 * @CreateTime: 2019/11/27 4:43
 **/
@Data
public class HttpXmlResponse {
    private FullHttpResponse response;
    private Object body;

    public HttpXmlResponse(FullHttpResponse response, Object body) {
        this.response = response;
        this.body = body;
    }

    @Override
    public String toString() {
        return "HttpXmlResponse{" +
                "response=" + response +
                ", body=" + body +
                '}';
    }
}
