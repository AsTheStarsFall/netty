package com.study.netty.consume;


import com.study.netty.api.IRpcHelloService;
import com.study.netty.api.IRpcService;
import com.study.netty.consume.proxy.RpcProxy;

/**
 * {@link}
 *
 * @Desc: RpcConsume消费者
 * @Author: thy
 * @CreateTime: 2019/8/7 3:27
 **/
public class RpcConsume {

    public static void main(String[] args) {
        RpcProxy rpcProxy = new RpcProxy();
        IRpcService service = rpcProxy.create(IRpcService.class,"localhost",8080);
//        IRpcHelloService service = rpcProxy.create(IRpcHelloService.class,"localhost",8080);

//        System.out.println(service.sayHello("ssss"));
        System.out.println(service.add(1,2));
    }
}
