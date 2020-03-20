package com.study.netty.chat.protocol;

/**
 * {@link}
 *
 * @Desc: 自定义通信协议, Instant Messaging Protocol
 * @Author: thy
 * @CreateTime: 2019/8/10 18:06
 **/
public enum IMP {
    /**
     * 系统消息
     */
    SYSTEM("SYSTEM"),
    /**
     * 登录指令
     */
    LOGIN("LOGIN"),
    /**
     * 登出指令
     */
    LOGOUT("LOGOUT"),
    /**
     * 聊天消息
     */
    CHAT("CHAT"),
    /**
     * 送鲜花
     */
    FLOWER("FLOWER");

    private String name;

    IMP(String name) {
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public static boolean isIMP(String content){
        return content.matches("^\\[(SYSTEM|LOGIN|LOGIN|CHAT)\\]");
    }

}
