package com.tianhy.v2;

import com.tianhy.dto.User;
import com.tianhy.service.IHelloService;

/**
 * {@link}
 *
 * @Desc: 接口实现类
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
@RpcService(value = IHelloService.class,version = "v1.0")
public class HelloServiceImpl2 implements IHelloService {
    @Override
    public String sayHello(String content) {
        System.out.println("v1.0 request sayHello:" +  content);
        return "v1.0 say hello :" + content;
    }

    @Override
    public String saveUser(User user) {
        System.out.println("v1.0 request saveUser:" +  user);
        return "v1.0 SUCCESS";
    }
}
