package com.study.netty.protocol.http.fileServer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * {@link}
 *
 * @Desc: 文件服务器
 * @Author: thy
 * @CreateTime: 2019/11/25 22:45
 **/
public class HttpFileServer {

    //文件路径
    private static final String DEFAULT_URL = "/src/main/java/com/study/netty/";

    public void run(int port, String url) throws InterruptedException, UnknownHostException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //http协议编解码器
                            //http请求解码器
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            /**
                             * aggregator:聚合
                             * HttpObjectAggregator作用：将多个消息转换为单一的FullHttpRequest或者FullHttpResponse对象，
                             * 原因是HTTP解码器在每个HTTP消息中会生成多个消息对象：
                             * 1、HttpRequest/HttpResponse
                             * 2、HttpContent
                             * 3、LastHttpContent
                             */
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            //http响应编码器
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            //ChunkedWriteHandler作用是：支持异步发送大的码流（比如大的文件传输，但不占用过多的内存，防止内存溢出）
                            ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());

                            ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
                        }
                    });

            ChannelFuture channelFuture = bootstrap.bind(port).sync();
            System.out.println("文件目录服务器启动，地址：" + InetAddress.getLocalHost() + ":" + port + url);
            channelFuture.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException, UnknownHostException {
        int port = 8080;
        new HttpFileServer().run(port, DEFAULT_URL);
    }
}
