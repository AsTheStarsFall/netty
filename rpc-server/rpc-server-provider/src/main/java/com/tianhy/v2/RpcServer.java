package com.tianhy.v2;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * {@link}
 *
 * @Desc: RPC服务端
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
@Component
public class RpcServer implements ApplicationContextAware, InitializingBean {

    //线程池
    ExecutorService es = Executors.newCachedThreadPool();

    //
    Map<String, Object> handlerMap = new HashMap<>();

    //读取配置文件
    ResourceBundle resourceBundle = ResourceBundle.getBundle("application");

    private String port;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        //通过注解获取到beans
        Map<String, Object> beansWithAnnotation = applicationContext.getBeansWithAnnotation(RpcService.class);
        if (!beansWithAnnotation.isEmpty()) {
            for (Object bean : beansWithAnnotation.values()) {
                RpcService annotation = bean.getClass().getAnnotation(RpcService.class);
                String name = annotation.value().getName();
                String version = annotation.version();
                if (StringUtils.isNotBlank(version)) {
                    name += "-" + version;
                }
                handlerMap.put(name, bean);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ServerSocket serverSocket = null;
        try {
//            System.out.println(properties.getProperty("rpc_server_port"));

            port = resourceBundle.getString("rpc_server_port");
//            System.out.println(port);
            //创建ServerSocket实例
            serverSocket = new ServerSocket(Integer.valueOf(port));
            //轮询的方式接受客户端的请求
            while (true) {
                Socket accept = serverSocket.accept();
                //数据流的处理交给processHandler处理
                es.execute(new ProcessHandler(accept, handlerMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
