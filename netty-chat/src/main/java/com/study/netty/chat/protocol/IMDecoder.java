package com.study.netty.chat.protocol;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.lang3.StringUtils;
import org.msgpack.MessagePack;
import org.msgpack.MessageTypeException;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link}
 *
 * @Desc: 自定义IM协议解码器
 * @Author: thy
 * @CreateTime: 2019/8/10 18:08
 **/
public class IMDecoder extends ByteToMessageDecoder {

    //定义匹配规则
    private Pattern pattern = Pattern.compile("\\[(.*)\\](\\s\\-\\s(.*))?");

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        try {
            //可读字节长度
            final int len = in.readableBytes();
            final byte[] array = new byte[len];
            String content = new String(array, in.readerIndex(), len);

            //解析
            if (StringUtils.isNoneBlank(content)) {
                if(!(IMP.isIMP(content))){
                    ctx.channel().pipeline().remove(this);
                    return;
                }
                in.getBytes(in.readerIndex(),array,0,len);
                //解析字符串
                out.add(new MessagePack().read(array,IMMessage.class));
                in.clear();
            }
        } catch (MessageTypeException e) {
            e.printStackTrace();
            ctx.channel().pipeline().remove(this);
        }
    }

    //将字符串解析成自定义协议
    public IMMessage decode(String msg){
        if(StringUtils.isBlank(msg)){return null;}
        try {
            Matcher m = pattern.matcher(msg);
            String header = "";
            String content = "";
            if(m.matches()){
                header = m.group(1);
                content = m.group(3);
            }
            String [] headers = header.split("\\]\\[");
            long time = 0;
            try{ time = Long.parseLong(headers[1]); } catch(Exception e){}
            String nickName = headers[2];
            //昵称最多十个字
            nickName = nickName.length() < 10 ? nickName : nickName.substring(0, 9);

            if(msg.startsWith("[" + IMP.LOGIN.getName() + "]")){
                return new IMMessage(headers[0],headers[3],time,nickName);
            }else if(msg.startsWith("[" + IMP.CHAT.getName() + "]")){
                return new IMMessage(headers[0],time,nickName,content);
            }else if(msg.startsWith("[" + IMP.FLOWER.getName() + "]")){
                return new IMMessage(headers[0],headers[3],time,nickName);
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
