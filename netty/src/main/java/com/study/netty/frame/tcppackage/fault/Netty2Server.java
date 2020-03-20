package com.study.netty.frame.tcppackage.fault;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * {@link}
 *
 * @Desc: 服务端（未考虑粘包/拆包）
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class Netty2Server {

    public void bind(int port) throws InterruptedException {
        //配置服务端的NIO 线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //用于启动NIO服务端的启动类
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //ChildChannelHandler I/O事件处理类
                    .childHandler(new ChildChannelHandler());
            //绑定端口，同步等待成功
            ChannelFuture future = bootstrap.bind(port).sync();
            //等待服务端监听端口关闭
            future.channel().closeFuture().sync();
        } finally {
            //优雅退出，释放线程池资源
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }


    private class ChildChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new Netty2ServerHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int port = 6666;
        Netty2Server server = new Netty2Server();
        server.bind(port);
    }
}
