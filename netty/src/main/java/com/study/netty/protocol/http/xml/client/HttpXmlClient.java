package com.study.netty.protocol.http.xml.client;

import com.study.netty.protocol.http.xml.codec.HttpXmlRequestEncoder;
import com.study.netty.protocol.http.xml.codec.HttpXmlResponseDecoder;
import com.study.netty.protocol.http.xml.pojo.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;

import java.net.InetSocketAddress;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/27 9:21
 **/
public class HttpXmlClient {

    public void connect(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group(bossGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //HttpResponse解码器
                            ch.pipeline().addLast("http-decoder", new HttpResponseDecoder());
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            //HttpResponse+xml解码器
                            ch.pipeline().addLast("xml-decoder",
                                    new HttpXmlResponseDecoder(Order.class, true));
                            //HttpRequest编码器
                            ch.pipeline().addLast("http-encoder", new HttpRequestEncoder());
                            //HttpRequest+xml 编码器
                            ch.pipeline().addLast("xml-encoder", new HttpXmlRequestEncoder());
                            ch.pipeline().addLast("httpXmlClientHandler", new HttpXmlClientHandler());
                        }
                    });

            ChannelFuture future = b.connect(new InetSocketAddress(port)).sync();
            future.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 1126;
        new HttpXmlClient().connect(port);

    }
}
