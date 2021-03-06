package com.study.netty.nettydemo.netty0;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @Desc: NettyClient客户端
 * @Author: thy
 * @CreateTime: 2018/11/15
 **/
public class NettyClient implements Runnable {


    @Override
    public void run() {
        EventLoopGroup group=new NioEventLoopGroup();
        try {
            Bootstrap bootstrap=new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY,true)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    ChannelPipeline pipeline=channel.pipeline();
                    pipeline.addLast("frameEncoder", new LengthFieldPrepender(4));
                    pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
                    pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
                    pipeline.addLast("handler", new MyClient());
                }
            });

            for (int i =0 ; i < 10 ; i++){
                ChannelFuture future=bootstrap.connect("127.0.0.1",6666).sync();
                future.channel().writeAndFlush("hello server !" + Thread.currentThread().getName()+"---"+i);
            }


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        for (int i = 0;i < 3 ;i++ ){
            new Thread(new NettyClient(),">>> this thread "+i).start();
        }
    }
}
