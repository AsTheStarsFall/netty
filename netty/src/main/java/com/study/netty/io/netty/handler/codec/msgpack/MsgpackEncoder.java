package com.study.netty.io.netty.handler.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.msgpack.MessagePack;

/**
 * {@link}
 *
 * @Desc: MessagePack 编码器
 * @Author: thy
 * @CreateTime: 2019/11/11 22:19
 **/
public class MsgpackEncoder extends MessageToByteEncoder<Object> {

    //负责将Object类型的POJO对象编码为byte数组，然后写入ByteBuf
    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        MessagePack messagePack = new MessagePack();
        messagePack.register(msg.getClass());
        //序列化
        byte[] raw = messagePack.write(msg);
        //写入ByteBuf
        out.writeBytes(raw);
    }
}
