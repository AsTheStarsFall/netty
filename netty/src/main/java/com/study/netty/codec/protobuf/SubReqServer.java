package com.study.netty.codec.protobuf;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * {@link}
 *
 * @Desc: 订购服务端
 * @Author: thy
 * @CreateTime: 2019/11/12 4:40
 **/
public class SubReqServer {

    public void bind(int port) throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChildChannelHandler());

            ChannelFuture channelFuture = b.bind(port).sync();
            System.out.println("SubReqServer started on :" + port);
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            //粘包/半包处理
            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
            //ProtoBuf解码器，参数是为了指明需要解码的类型
            ch.pipeline().addLast(new ProtobufDecoder(
                    SubscribeReqProto.SubscribeReq.getDefaultInstance()
            ));
            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
            //ProtoBuf编码器
            ch.pipeline().addLast(new ProtobufEncoder());
            ch.pipeline().addLast(new SubReqServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SubReqServer subReqServer = new SubReqServer();
        subReqServer.bind(9311);
    }
}
