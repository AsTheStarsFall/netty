package com.study.netty.codec.protobuf;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.*;

/**
 * {@link}
 *
 * @Desc: 订购客户端
 * @Author: thy
 * @CreateTime: 2019/11/12 5:09
 **/
public class SubReqClient {
    public void connect(String host, int port) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChiledChannelHandler());

            ChannelFuture sync = b.connect(host, port).sync();
            sync.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    private class ChiledChannelHandler extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            //粘包/半包处理
            ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
            //ProtoBuf解码器，参数是为了指明需要解码的类型
            ch.pipeline().addLast(new ProtobufDecoder(
                    SubscribeRespProto.SubscribeResp.getDefaultInstance()
            ));
            ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
            //ProtoBuf编码器
            ch.pipeline().addLast(new ProtobufEncoder());

            ch.pipeline().addLast(new SubReqClientHandler());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SubReqClient client = new SubReqClient();
        client.connect("127.0.0.1", 9311);
    }
}
