package com.study.netty.codec.msgpack;

import com.study.netty.frame.serialize.UserInfo;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * {@link}
 *
 * @Desc: 客户端处理器handler
 * @Author: thy
 * @CreateTime: 2019/11/11 23:04
 **/
public class EchoClientHandler extends ChannelHandlerAdapter {

    private final int sendNum;

    public EchoClientHandler(int sendNum) {
        this.sendNum = sendNum;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        UserInfo[] userInfos = UserInfo();
        for (UserInfo userInfo : userInfos) {
            ctx.writeAndFlush(userInfo);
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("Client receive the msgpack message :" + msg);
//        ctx.write(msg);
    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//        //写入SocketChannel
//        ctx.flush();
//    }

    private UserInfo[] UserInfo() {
        UserInfo[] userInfos = new UserInfo[sendNum];
        UserInfo userInfo = null;
        for (int i = 0; i < sendNum; i++) {
            userInfo = new UserInfo();
            userInfo.setUserName("netty ---> " + i);
            userInfo.setUserID(i);
            userInfos[i] = userInfo;
        }
        return userInfos;
    }
}
