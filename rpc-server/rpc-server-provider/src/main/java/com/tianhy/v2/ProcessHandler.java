package com.tianhy.v2;

import com.tianhy.request.RpcRequest;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * {@link}
 *
 * @Desc: 请求处理类
 * 两个要素：
 * 1、封装socket
 * 2、反射调用服务
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class ProcessHandler implements Runnable {

    private Socket socket;
    private Map<String, Object> handlerMap;

    public ProcessHandler(Socket socket, Map<String, Object> handlerMap) {
        this.socket = socket;
        this.handlerMap = handlerMap;
    }

    @Override
    public void run() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            //从输入流中获取到请求的类名、方法名、参数
            ois = new ObjectInputStream(socket.getInputStream());
            //反序列化为自定义请求实体
            RpcRequest rpcRequest = (RpcRequest) ois.readObject();
            //反射调用
            Object result = invoke(rpcRequest);

            //写入输出流
            oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(result);
            oos.flush();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 从请求体获取要调用的方法，通过反射执行
     * @param rpcRequest 数据通信封装请求体
     * @return
     */
    private Object invoke(RpcRequest rpcRequest) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String clazzName = rpcRequest.getClassName();
        String version = rpcRequest.getVersion();

        if (!StringUtils.isEmpty(version)) {
            clazzName += "-" + version;
        }

        Object service = handlerMap.get(clazzName);
        if (service == null) {
            throw new RuntimeException("service not found :" + clazzName);
        }

        Object[] parameters = rpcRequest.getParameters();
        Method method = null;
        if (parameters != null) {
            Class<?>[] types = new Class[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                types[i] = parameters[i].getClass();
            }
            Class<?> clazz = Class.forName(rpcRequest.getClassName());
            method = clazz.getMethod(rpcRequest.getMethodName(), types);
        } else {
            Class<?> clazz = Class.forName(rpcRequest.getClassName());
            method = clazz.getMethod(rpcRequest.getMethodName());
        }
        //真正的反射调用
        Object result = method.invoke(service, parameters);
        return result;
    }
}
