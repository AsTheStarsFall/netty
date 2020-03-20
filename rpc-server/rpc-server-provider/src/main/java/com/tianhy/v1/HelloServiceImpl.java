package com.tianhy.v1;

import com.tianhy.dto.User;
import com.tianhy.service.IHelloService;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class HelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String content) {
        System.out.println("request sayHello:" +  content);
        return "say hello :" + content;
    }

    @Override
    public String saveUser(User user) {
        System.out.println("request saveUser:" +  user);
        return "SUCCESS";
    }
}
