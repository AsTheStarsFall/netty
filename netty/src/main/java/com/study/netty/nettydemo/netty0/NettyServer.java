package com.study.netty.nettydemo.netty0;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;
import lombok.extern.java.Log;

/**
 * @Desc: NettyServer服务端
 * @Author: thy
 * @CreateTime: 2018/11/15
 **/
@Log
public class NettyServer {

    private static final String IP = "127.0.0.1";
    private static final int PORT = 6666;
    //CUP核心数
    private static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2;
    //线程数
    private static final int BIZTHREADSIZE = 100;
    //用于服务端接受客户端连接
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    //用于SocketChannel读写数据
    private static final EventLoopGroup workGroup = new NioEventLoopGroup(BIZTHREADSIZE);


    public static void start() throws InterruptedException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4,
                                0, 4));
                        pipeline.addLast(new StringDecoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new StringEncoder(CharsetUtil.UTF_8));
                        pipeline.addLast(new TCPServerHandler());
                    }
                });

        ChannelFuture channelFuture = serverBootstrap.bind(IP, PORT).sync();
        channelFuture.channel().closeFuture().sync();
        System.out.println("server start");

    }

    public static void main(String[] args) throws InterruptedException {
        NettyServer.start();
    }

}
