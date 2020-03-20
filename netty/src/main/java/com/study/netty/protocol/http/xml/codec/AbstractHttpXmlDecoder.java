package com.study.netty.protocol.http.xml.codec;

import com.study.netty.protocol.http.xml.pojo.Order;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.jibx.runtime.*;

import java.io.StringReader;
import java.nio.charset.Charset;

/**
 * {@link}
 *
 * @Desc: HTTP+XML 解码抽象类
 * @Author: thy
 * @CreateTime: 2019/11/27 4:41
 **/
public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {
    private IBindingFactory bindFactory = null;
    private StringReader reader = null;
    private Class<?> clazz;
    private boolean isPrint;
    private final static String CHARSET_NAME = "UTF-8";
    private final static Charset UTF_8 = Charset.forName(CHARSET_NAME);

    protected AbstractHttpXmlDecoder(Class<?> clazz) {
        this(clazz, false);
    }

    protected AbstractHttpXmlDecoder(Class<?> clazz, boolean isPrint) {
        this.clazz = clazz;
        this.isPrint = isPrint;
    }

    //解码
    protected Object decode0(ChannelHandlerContext ctx, ByteBuf body) throws JiBXException {
        bindFactory = BindingDirectory.getFactory(clazz);
        //xml格式的数据
        String content = body.toString(UTF_8);
        if (isPrint) {
            System.out.println("The Body is: " + content);
        }
        reader = new StringReader(content);
        IUnmarshallingContext context = bindFactory.createUnmarshallingContext();
        //转换成POJO对象
        Order order = (Order) context.unmarshalDocument(reader);
        return order;
    }

    @Skip
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }

}
