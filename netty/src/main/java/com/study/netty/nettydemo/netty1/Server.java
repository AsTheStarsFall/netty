package com.study.netty.nettydemo.netty1;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @Desc:
 * @Author: thy
 * @CreateTime: 2018/11/15
 **/
public class Server {

    public static void main(String[] args) {
        //创建两个EventLoopGruop对象
        //创建boss线程组 用于服务器接受客户端连接
        EventLoopGroup bossGruop = new NioEventLoopGroup(1);
        //创建work线程组 用于进行SocketChannel的数据读写
        EventLoopGroup workGruop = new NioEventLoopGroup();

        try {
            //创建ServerBootStrap对象
            ServerBootstrap bootstrap = new ServerBootstrap();
            //设置使用的eventLoopGroup
            bootstrap.group(bossGruop, workGruop)
                    //设置要被实例化的类 NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //handler 处理服务端逻辑
                    .handler(new LoggingHandler(LogLevel.INFO))
                    //设置连入服务端的client的SocketChannel的处理器
                    .childHandler(new ServerInitalizer());

            //绑定端口，并同步等待成功
            ChannelFuture future = bootstrap.bind(8888);
            //监听服务端关闭，并阻塞等待
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGruop.shutdownGracefully();
            workGruop.shutdownGracefully();
        }
    }
}
