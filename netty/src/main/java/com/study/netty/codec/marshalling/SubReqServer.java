package com.study.netty.codec.marshalling;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * {@link}
 *
 * @Desc: Marshalling服务端
 * @Author: thy
 * @CreateTime: 2019/11/26 5:04
 **/
public class SubReqServer {

    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    //accept queue的大小
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new MarshallingChildChannelHandler());
            ChannelFuture channelFuture = b.bind(port).sync();
            System.out.println("server started on :" + port);
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class MarshallingChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            //marshalling解码器
            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
            //marshalling编码器
            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
            ch.pipeline().addLast(new MarshallingServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 1126;
        new SubReqServer().bind(port);
    }
}
