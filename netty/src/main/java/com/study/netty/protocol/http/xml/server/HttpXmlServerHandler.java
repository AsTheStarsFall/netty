package com.study.netty.protocol.http.xml.server;

import com.alibaba.fastjson.JSON;
import com.study.netty.protocol.http.xml.codec.HttpXmlRequest;
import com.study.netty.protocol.http.xml.codec.HttpXmlResponse;
import com.study.netty.protocol.http.xml.pojo.Address;
import com.study.netty.protocol.http.xml.pojo.Order;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import org.json.simple.JSONObject;

import java.util.*;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/27 8:58
 **/
public class HttpXmlServerHandler extends SimpleChannelInboundHandler<HttpXmlRequest> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpXmlRequest msg) throws Exception {
        FullHttpRequest request = msg.getRequest();
        //order--body
        Order order = (Order) msg.getBody();
        System.out.println("Http server receive request : " + order);
        System.out.println("Request headers: " + request.headers().names());
        System.out.println(JSON.toJSONString(request.headers().entries()));
        //创建订单信息
        doOrder(order);
        //order作为参数构建HttpXmlResponse进行传输
        ChannelFuture future = ctx.writeAndFlush(new HttpXmlResponse(null, order));
        //如果断开连接，关闭通道
        if (!HttpHeaders.isKeepAlive(request)) {
            future.addListener(future1 -> ctx.close());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (ctx.channel().isActive()) {
            sendError(ctx, HttpResponseStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
                Unpooled.copiedBuffer("Failure: " + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void doOrder(Order order) {
        order.getCustomer().setFirstName("狄");
        order.getCustomer().setLastName("仁杰");
        List<String> midNames = new ArrayList<String>();
        midNames.add("李元芳");
        order.getCustomer().setMiddleNames(midNames);
        Address address = order.getBillTo();
        address.setCity("洛阳");
        address.setCountry("大唐");
        address.setState("河南道");
        address.setPostCode("123456");
        order.setBillTo(address);
        order.setShipTo(address);
    }
}
