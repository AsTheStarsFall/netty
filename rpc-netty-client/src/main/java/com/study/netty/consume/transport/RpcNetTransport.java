package com.study.netty.consume.transport;

import com.study.netty.protocol.SelfDefineProtocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.*;

import java.lang.reflect.Method;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/8/7 3:28
 **/
public class RpcNetTransport {
    private String host;
    private int port;

    public RpcNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object send(Class<?> clazz, Method method,Object[] args){

        //封装传输协议
        SelfDefineProtocol defineProtocol = new SelfDefineProtocol();
//        defineProtocol.setClassName(method.getDeclaringClass().getName());
        defineProtocol.setClassName(method.getDeclaringClass().getName());
        defineProtocol.setMethodName(method.getName());
        defineProtocol.setParameters(method.getParameterTypes());
        defineProtocol.setValues(args);

        //网络通信
        TransportHandler netTransport = new TransportHandler();

        //Netty client
        EventLoopGroup workGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();

        try {
        bootstrap.group(workGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0,
                                4, 0, 4));
                        ch.pipeline().addLast("frameEncoder",new LengthFieldPrepender(4));
//                        ch.pipeline().addLast(new HttpResponseEncoder());
//                        ch.pipeline().addLast(new HttpRequestDecoder());
                       // 参数编解码器
                        ch.pipeline().addLast("encoder",new ObjectEncoder());
                        ch.pipeline().addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
                        ch.pipeline().addLast("handler",netTransport);
                    }
                });

        //客户端这个地方connect
        ChannelFuture future = bootstrap.connect(host, port).sync();
        future.channel().writeAndFlush(defineProtocol).sync();
        future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
        }
        return netTransport.getResponse();
    }



}
