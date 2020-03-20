package com.study.netty.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * {@link}
 *
 * @Desc: 自定义协议,必须实现序列化
 * @Author: thy
 * @CreateTime: 2019/8/7 1:25
 **/
@Data
public class SelfDefineProtocol implements Serializable {

    //4个字节长度
    private String className;
    private String methodName;
    private Class<?> [] parameters;
    private Object[]values;

}
