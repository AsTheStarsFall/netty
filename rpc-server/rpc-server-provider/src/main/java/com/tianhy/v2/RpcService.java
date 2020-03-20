package com.tianhy.v2;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/6/23
 **/
//注解加到什么类型上，类/接口
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

    //服务接口
    Class<?> value();

    //版本号
    String version() default "";
}
