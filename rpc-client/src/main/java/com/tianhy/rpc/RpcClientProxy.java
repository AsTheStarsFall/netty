package com.tianhy.rpc;

import java.lang.reflect.Proxy;

/**
 * {@link}
 *
 * @Desc: 客户端代理类
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class RpcClientProxy {

    public <T> T clientProxy(final Class<?> interfaceClazz, final String host, final int port) {

        return (T) Proxy.newProxyInstance(interfaceClazz.getClassLoader(),
                new Class[]{interfaceClazz}, new RemoteInvocationHandler(host,port));

//        Class<?> clazz = interfaceClazz.getClass();
//        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), clazz.getInterfaces(), new RemoteInvocationHandler(host,port));

    }


}
