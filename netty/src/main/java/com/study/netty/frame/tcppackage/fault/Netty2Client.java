package com.study.netty.frame.tcppackage.fault;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * {@link}
 *
 * @Desc: 客户端（未考虑粘包/拆包）
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class Netty2Client {

    public void connect(int port, String host) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group).channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        //当创建NioSocketChannel成功后，在初始化时，将它的ChannelHandler设置到ChannelPipeline中
                        //用于处理网络I/O事件
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //Netty2ClientHandler I/O事件处理
                            ch.pipeline().addLast(new Netty2ClientHandler());
                        }
                    });

            //发起异步连接操作
            ChannelFuture future = bootstrap.connect(host, port).sync();
            //同步，等待客户端链路关闭
            future.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
        int port = 6666;
        new Netty2Client().connect(port, "127.0.0.1");
    }
}
