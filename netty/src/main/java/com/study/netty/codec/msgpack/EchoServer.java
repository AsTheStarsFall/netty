package com.study.netty.codec.msgpack;

import com.study.netty.io.netty.handler.codec.msgpack.MsgpackDecoder;
import com.study.netty.io.netty.handler.codec.msgpack.MsgpackEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * {@link}
 *
 * @Desc: messagepack编解码的服务端
 * @Author: thy
 * @CreateTime: 2019/11/11 23:40
 **/
public class EchoServer {

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
                    .childHandler(new ChildChannelHandler());

            ChannelFuture channelFuture = b.bind(port).sync();
            System.out.println("server started on :" + port);
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            /**
             * LengthFieldBasedFrameDecoder 实现对粘包/半包的处理
             * 使后面的MsgpackDecoder接收到整包消息
             */
            ch.pipeline().addLast("frameDecoder",
                    new LengthFieldBasedFrameDecoder(65535,
                            0, 2,
                            0, 2));
            ch.pipeline().addLast("msgpack decoder", new MsgpackDecoder());
            //LengthFieldPrepender在ByteBuf之前增加2个字节的消息长度字段
            ch.pipeline().addLast("frameEncoder", new LengthFieldPrepender(2));
            ch.pipeline().addLast("msgpack encoder", new MsgpackEncoder());
            ch.pipeline().addLast(new EchoServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 1126;
        EchoServer server = new EchoServer();
        server.bind(port);
    }
}
