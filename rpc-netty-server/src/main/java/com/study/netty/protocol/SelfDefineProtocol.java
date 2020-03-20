package com.study.netty.protocol;

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

    private String className;
    private String methodName;
    private Class<?> [] parameters;
    private Object[]values;

}
