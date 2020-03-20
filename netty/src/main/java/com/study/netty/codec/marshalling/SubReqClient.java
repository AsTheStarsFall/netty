package com.study.netty.codec.marshalling;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * {@link}
 *
 * @Desc: Marshalling客户端
 * @Author: thy
 * @CreateTime: 2019/11/26 5:19
 **/
public class SubReqClient {
    public void connect(String host, int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new MarshallingClientChannelHandler());
            ChannelFuture sync = b.connect(host, port).sync();
            sync.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private class MarshallingClientChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            //marshalling解码器
            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
            //marshalling编码器
            ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
            ch.pipeline().addLast(new MarshallingClientHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 1126;
        new SubReqClient().connect("localhost", port);
    }
}
