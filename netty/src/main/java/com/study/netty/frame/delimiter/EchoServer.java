package com.study.netty.frame.delimiter;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * {@link}
 *
 * @Desc: EchoServer服务端收到EchoClient消息后，将其打印出来，然后将原始信息返回客户端，消息以“$_”作为分隔符
 * @Author: thy
 * @CreateTime: 2019/11/5 6:50
 **/
public class EchoServer {

    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //启动类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChildChannelHandler());

            ChannelFuture future = bootstrap.bind(port).sync();
            System.out.println("EchoServer started on port :" + port);
            future.channel().closeFuture().sync();

        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {

            //todo DelimiterBasedFrameDecoder
            //设置分隔符
            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
            //maxFrameLength单条消息的最大长度
            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
            //将ByteBuffer解码成字符串对象
            ch.pipeline().addLast(new StringDecoder());
            //EchoServerHandler接收到的msg消息就是解码后的字符串
            ch.pipeline().addLast(new EchoServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 7777;
        EchoServer server = new EchoServer();
        server.bind(port);
    }
}
