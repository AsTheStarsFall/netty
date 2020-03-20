package com.study.netty.rpc.consume;

import com.study.netty.rpc.api.IRpcHelloService;
import com.study.netty.rpc.api.IRpcService;
import com.study.netty.rpc.consume.proxy.RpcProxy;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/8/7 3:27
 **/
public class RpcConsume {

    public static void main(String[] args) {
        RpcProxy rpcProxy = new RpcProxy();
        IRpcService service1 = rpcProxy.create(IRpcService.class,"localhost",8080);
        IRpcHelloService service = rpcProxy.create(IRpcHelloService.class,"localhost",8080);

        System.out.println(service.sayHello("ssss"));

        System.out.println(service1.add(1,2));
    }
}
