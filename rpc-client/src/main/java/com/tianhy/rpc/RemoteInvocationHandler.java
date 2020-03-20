package com.tianhy.rpc;

import com.tianhy.request.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class RemoteInvocationHandler implements InvocationHandler {
    private String host;
    private int port;

    public RemoteInvocationHandler(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        //构建请求体
        RpcRequest rpcRequest = new RpcRequest();
        //类名
        rpcRequest.setClassName(method.getDeclaringClass().getName());
        //方法名
        rpcRequest.setMethodName(method.getName());
        //参数
        rpcRequest.setParameters(args);
        //版本
        rpcRequest.setVersion("v1.0");

        //远程通信
        RpcNetTransport rpcNetTransport = new RpcNetTransport(host,port);
        Object result = rpcNetTransport.send(rpcRequest);
        return result;
    }
}
