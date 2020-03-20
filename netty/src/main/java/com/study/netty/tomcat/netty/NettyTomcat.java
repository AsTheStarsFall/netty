package com.study.netty.tomcat.netty;

import com.study.netty.tomcat.netty.http.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

import java.io.FileInputStream;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Properties;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/8/6 7:24
 **/
public class NettyTomcat {
    //1、配置默认端口8080,ServerSocket,ip:localhost
    private int port = 8080;
    private ServerSocket serverSocket;
    //url-pattern 和 Servlet映射
    private HashMap<String, MyServlet> servletMapping = new HashMap<String, MyServlet>();
    //配置文件
    private Properties webXml = new Properties();

    /**
     * 2、配置web.xml,封装的servlet继承HttpServlet
     * servlet-name
     * servlet-class
     * url-pattern
     */


    //初始化
    public void init() {
        //3、读取配置文件，url-pattern与servlet建立映射关系
        try {
            String path = this.getClass().getResource("/").getPath();
            FileInputStream fis = new FileInputStream(path + "web.properties");

            webXml.load(fis);

            for (Object k : webXml.keySet()) {
                String key = k.toString();
                if (key.endsWith(".url")) {
                    String servletName = key.replaceAll("\\.url$", "");
                    String url = webXml.getProperty(key);
                    String className = webXml.getProperty(servletName + ".className");
                    MyServlet servlet = (MyServlet) Class.forName(className).newInstance();
                    servletMapping.put(url, servlet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        init();

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new HttpResponseEncoder());
                            ch.pipeline().addLast(new HttpRequestDecoder());
                            ch.pipeline().addLast(new NettyTomcatHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);

            ChannelFuture channelFuture = serverBootstrap.bind(this.port).sync();
            System.out.println("NettyTomcat 已经启动，端口：8080");
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }

    public class NettyTomcatHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

            if (msg instanceof HttpRequest) {
                HttpRequest req = (HttpRequest) msg;
                MyRequest request = new MyRequest(ctx, req);
                MyResponse response = new MyResponse(ctx);

                String uri = request.getUri();

                if (servletMapping.containsKey(uri)) {
                    servletMapping.get(uri).service(request, response);
                } else {
                    response.write("404 Not Found");

                }
            }
        }
    }


}
