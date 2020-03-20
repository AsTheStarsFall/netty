package com.study.netty.protocol.http.xml.server;

import com.study.netty.protocol.http.xml.codec.*;
import com.study.netty.protocol.http.xml.pojo.Order;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/27 8:43
 **/
public class HttpXmlServer {

    public void run(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //HttpRequest解码器
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            ch.pipeline().addLast("http-aggregator",
                                    new HttpObjectAggregator(65536));
                            //HttpRequest+xml 解码器
                            ch.pipeline().addLast("xml-decoder",
                                    new HttpXmlRequestDecoder(Order.class, true));
                            //HttpResponse编码器
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            //HttpResponse+xml 编码器
                            ch.pipeline().addLast("xml-encoder", new HttpXmlResponseEncoder());

                            ch.pipeline().addLast("httpXmlServerHandler", new HttpXmlServerHandler());
                        }
                    });
            ChannelFuture future = b.bind(new InetSocketAddress(port)).sync();
            System.out.println("HTTP订购服务器启动，网址是 : " + "http://localhost:" + port);
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();

        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 1126;
        new HttpXmlServer().run(port);
    }
}
