package com.study.netty.provider;


import com.study.netty.api.IRpcHelloService;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/8/7 1:03
 **/
public class RpcHelloServiceImpl implements IRpcHelloService {
    @Override
    public String sayHello(String s) {
        return "hello "+ s;
    }
}
