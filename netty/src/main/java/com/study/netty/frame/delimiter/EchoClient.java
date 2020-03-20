package com.study.netty.frame.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/5 7:19
 **/
public class EchoClient {
    public void connect(String host, int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            //启动类
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //设置分隔符
                            ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
                            //maxFrameLength单条消息的最大长度
                            ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
                            //将ByteBuffer解码成字符串对象
                            ch.pipeline().addLast(new StringDecoder());
                            //EchoClientHandler接收到的msg消息就是解码后的字符串
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });

            ChannelFuture future = bootstrap.connect(host, port).sync();
            System.out.println("EchoClient connected on :" + host + ":" + port);
            future.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 7777;
        EchoClient client = new EchoClient();
        client.connect("localhost", port);
    }
}
