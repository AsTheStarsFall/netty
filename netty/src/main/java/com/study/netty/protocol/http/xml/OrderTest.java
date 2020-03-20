package com.study.netty.protocol.http.xml;

import com.study.netty.protocol.http.xml.pojo.Order;
import com.study.netty.protocol.http.xml.pojo.OrderFactory;
import org.jibx.runtime.*;

import java.io.*;

/**
 * {@link}
 *
 * @Desc: 订购测试
 * @Author: thy
 * @CreateTime: 2019/11/26 3:39
 **/
public class OrderTest {
    private IBindingFactory factory = null;
    private StringWriter writer = null;
    private StringReader reader = null;
    private final static String CHARSET_NAME = "UTF-8";

    //编码为xml格式
    private String encode2Xml(Order order) throws JiBXException, IOException {
        factory = BindingDirectory.getFactory(Order.class);
        writer = new StringWriter();
        IMarshallingContext mctx = factory.createMarshallingContext();
        mctx.setIndent(2);
        mctx.marshalDocument(order, CHARSET_NAME, null, writer);
        String xmlStr = writer.toString();
        writer.close();
        System.out.println(xmlStr.toString());
        return xmlStr;
    }

    //解码为pojo实体
    private Order decode2Order(String xmlBody) throws JiBXException {
        reader = new StringReader(xmlBody);
        IUnmarshallingContext uctx = factory.createUnmarshallingContext();
        Order order = (Order) uctx.unmarshalDocument(reader);
        return order;
    }

    public static void main(String[] args) throws JiBXException, IOException {
        OrderTest test = new OrderTest();
        Order order = OrderFactory.create(123);
        String body = test.encode2Xml(order);
        Order order2 = test.decode2Order(body);
        System.out.println(order2);

    }
}
