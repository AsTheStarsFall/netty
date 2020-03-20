package com.tianhy.v2;

import org.springframework.context.annotation.*;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/6/24
 **/
@Configuration
@ComponentScan(basePackages = "com.tianhy.v2")
public class SpringConfig {

    @Bean(name = "rpcServer")
    public RpcServer rpcServer() {
        return new RpcServer();
    }
}
