package com.study.netty.protocol.http.xml.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;
import org.jibx.runtime.*;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

/**
 * {@link}
 *
 * @Desc: HTTP+XML 编码抽象类
 * @Author: thy
 * @CreateTime: 2019/11/27 3:21
 **/
public abstract class AbstractHttpXmlEncoder<T> extends MessageToMessageEncoder<T> {
    IBindingFactory bindFactory = null;
    StringWriter writer = null;
    static final String CHARSET_NAME = "UTF-8";
    static final Charset UTF_8 = Charset.forName(CHARSET_NAME);

    //编码，将pojo转换为字节（pojo->xml->Bytebuf）
    protected ByteBuf encode0(ChannelHandlerContext ctx, Object body) throws JiBXException, IOException {
        //根据传入的pojo对象 创建工厂
        bindFactory = BindingDirectory.getFactory(body.getClass());
        writer = new StringWriter();
        IMarshallingContext context = bindFactory.createMarshallingContext();
        context.setIndent(2);
        context.marshalDocument(body, CHARSET_NAME, null, writer);
        //将pojo转换成xml
        String xmlStr = writer.toString();
        writer.close();
        writer = null;
        //将xml转换成ByteBuf
        ByteBuf byteBuf = Unpooled.copiedBuffer(xmlStr, UTF_8);
        return byteBuf;
    }

    @Skip
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }
}
