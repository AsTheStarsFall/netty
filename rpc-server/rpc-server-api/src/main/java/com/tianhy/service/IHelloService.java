package com.tianhy.service;

import com.tianhy.dto.User;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public interface IHelloService {

    String sayHello(String content);

    String saveUser(User user);

}
