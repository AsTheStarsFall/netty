package com.tianhy;

import com.tianhy.service.IHelloService;
import com.tianhy.v1.HelloServiceImpl;
import com.tianhy.v1.RpcServerProxy;
import com.tianhy.v2.SpringConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;


/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
//        IHelloService service = new HelloServiceImpl();
//        RpcServerProxy serverProxy = new RpcServerProxy();
//        serverProxy.publish(8888,service);

        //注解的方式
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        ((AnnotationConfigApplicationContext) context).start();

    }
}
