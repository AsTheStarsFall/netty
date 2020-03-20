package com.study.netty.codec.msgpack;

import com.study.netty.io.netty.handler.codec.msgpack.MsgpackDecoder;
import com.study.netty.io.netty.handler.codec.msgpack.MsgpackEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.*;

/**
 * {@link}
 *
 * @Desc: messagepack编解码的客户端
 * @Author: thy
 * @CreateTime: 2019/11/11 22:45
 **/
public class EchoClient {

    private final String host;
    private final int port;
    private final int senNum;

    public EchoClient(String host, int port, int senNum) {
        this.host = host;
        this.port = port;
        this.senNum = senNum;
    }

    public void run() throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .handler(new ChannelInitializer<SocketChannel>() {
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
                            ch.pipeline().addLast(new EchoClientHandler(senNum));
                        }
                    });

            //建立连接
            ChannelFuture connect = b.connect(host, port).sync();
            //等待客戶端链路关闭后退出main函数
            connect.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        EchoClient client = new EchoClient("localhost", 1126, 10);
        client.run();
    }
}
