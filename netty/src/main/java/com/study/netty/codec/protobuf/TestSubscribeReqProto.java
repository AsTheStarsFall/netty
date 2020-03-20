package com.study.netty.codec.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/12 4:06
 **/
public class TestSubscribeReqProto {
    private static SubscribeReqProto.SubscribeReq createSubscribeReq() {
        SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
        builder.setSubReqID(1);
        builder.setUserName("Lilinfeng");
        builder.setProductName("Netty Book");
        builder.setAddress("GuangDong GuangZhou");
        return builder.build();
    }
    //编码
    private static byte[] encode(SubscribeReqProto.SubscribeReq req) {
        return req.toByteArray();
    }
    //解码
    private static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeReqProto.SubscribeReq.parseFrom(body);
    }
    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq subscribeReq = createSubscribeReq();
        System.out.println("Before encode：" + subscribeReq.toString());
        long startTime = System.currentTimeMillis();
        byte[] bytes = encode(subscribeReq);
        long endTime = System.currentTimeMillis();
        System.out.println("encode cost time :" + (endTime - startTime) + "ms");
        System.out.println("bytes length :" + bytes.length);
        System.out.println("-----------------------------");
        SubscribeReqProto.SubscribeReq decode = decode(bytes);
        System.out.println("after decode :" + decode.toString());

        System.out.println("Assert equal :" + subscribeReq.equals(decode));

    }


}
