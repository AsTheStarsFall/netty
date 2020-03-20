package com.study.netty.consume.proxy;

import java.lang.reflect.Proxy;

/**
 * {@link}
 *
 * @Desc: Rpc消费者代理类，也就是客户端
 * @Author: thy
 * @CreateTime: 2019/8/7 2:48
 **/
public class RpcProxy {
    public <T> T create(final Class<?> clazz, final String host, final int port) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, new ProxyInvocationHandler(clazz,host, port));
    }
}
