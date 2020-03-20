package com.study.netty.io.netty.handler.codec.msgpack;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import org.msgpack.MessagePack;

import java.util.List;

/**
 * {@link}
 *
 * @Desc: MessagePack 解码器
 * @Author: thy
 * @CreateTime: 2019/11/11 22:26
 **/
public class MsgpackDecoder extends MessageToMessageDecoder<ByteBuf> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
        //从数据报 msg 中获取需要解码的 byte 数组
        final byte[] bytes;
        final int length = msg.readableBytes();
        bytes = new byte[length];
        //Transfers this buffer's data to the specified destination
        //bytes：目标字节数组
        msg.getBytes(msg.readerIndex(), bytes, 0, length);

        MessagePack messagePack = new MessagePack();
        //将其反序列化为Object对象，将解码后的对象加入到解码列表out中
        out.add(messagePack.read(bytes));
    }
}
