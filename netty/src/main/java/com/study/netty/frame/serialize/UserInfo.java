package com.study.netty.frame.serialize;

import java.io.Serializable;
import java.nio.ByteBuffer;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/11/6 19:02
 **/
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;

    private int userID;

    public UserInfo createUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserInfo createUserId(int userID) {
        this.userID = userID;
        return this;
    }

    //基于ByteBuffer的通用二进制编解码，编码结果仍是byte数组
    public byte[] codec() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] userNameBytes = this.userName.getBytes();
        byteBuffer.putInt(userNameBytes.length);
        byteBuffer.put(userNameBytes);
        byteBuffer.putInt(this.userID);

        byteBuffer.flip();
        userNameBytes = null;
        byte[] result = new byte[byteBuffer.remaining()];
        byteBuffer.get(result);
        return result;
    }

    public byte[] codec(ByteBuffer byteBuffer) {
        byteBuffer.clear();
        byte[] userNameBytes = this.userName.getBytes();
        byteBuffer.putInt(userNameBytes.length);
        byteBuffer.put(userNameBytes);
        byteBuffer.putInt(this.userID);

        byteBuffer.flip();
        userNameBytes = null;
        byte[] result = new byte[byteBuffer.remaining()];
        //transfers bytes from this buffer into the given destination array
        byteBuffer.get(result);
        return result;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
