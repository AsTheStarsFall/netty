package com.tianhy.rpc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/6/23
 **/
@Configuration
public class SpringConfig {

    //注入代理类
    @Bean(name = "rpcClientProxy")
    public RpcClientProxy proxyClient(){
        return new RpcClientProxy();
    }
}
