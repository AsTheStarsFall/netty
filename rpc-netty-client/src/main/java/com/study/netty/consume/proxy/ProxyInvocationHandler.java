package com.study.netty.consume.proxy;


import com.study.netty.consume.transport.RpcNetTransport;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/8/7 3:05
 **/
public class ProxyInvocationHandler implements InvocationHandler {
    private String host;
    private int port;
    private Class<?> clazz;

    public ProxyInvocationHandler(Class<?> clazz,String host, int port) {
        this.host = host;
        this.port = port;
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            //如果是实现类直接invoke
            if(method.getDeclaringClass().equals(Object.class)){
                return method.invoke(this,args);
            }else{
                //网络通信
                RpcNetTransport netTransport = new RpcNetTransport(host,port);
                Object send = netTransport.send(clazz, method, args);
                return send;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
