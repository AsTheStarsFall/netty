package com.study.netty.rpc.register;

import com.study.netty.rpc.protocol.SelfDefineProtocol;
import io.netty.channel.*;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@link}
 *
 * @Desc: 调用服务逻辑处理类
 * @Author: thy
 * @CreateTime: 2019/8/7 1:20
 **/
public class RpcRegisterHandler extends ChannelInboundHandlerAdapter {

    //服务映射
    public static ConcurrentHashMap<String,Object> serviceMapping = new ConcurrentHashMap<>();

    //所有服务
    private List<String> classNames = new ArrayList<>();

    public RpcRegisterHandler() {
        doScannerClass("com.study.netty.rpc.provider");
        doRegister();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        //重要的地方：将msg强转为自定义协议
        SelfDefineProtocol protocol = (SelfDefineProtocol) msg;
        if(serviceMapping.containsKey(protocol.getClassName())){
            Object clazz = serviceMapping.get(protocol.getClassName());
            Method method = clazz.getClass().getMethod(protocol.getMethodName(), protocol.getParameters());
            result = method.invoke(clazz,protocol.getValues());
        }
        ctx.write(result);
        ctx.flush();
        ctx.close();
    }


    //根据包名扫描服务
    private void doScannerClass(String packageName) {
        //拿到路径URL
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        //遍历路径下文件
        File file = new File(url.getFile());
        for (File f : file.listFiles()) {
            //如果是一个文件夹，继续遍历
            if(f.isDirectory()){
                doScannerClass(packageName +"."+ file.getName());
            }else{
                //如果不是，将其保存起来
                classNames.add(packageName +"."+ f.getName().replaceAll(".class","").trim());
            }
        }
    }

    //注册
    private void doRegister() {
        if(classNames.size() == 0){return;}
        try {
        for (String className : classNames) {
            Class<?> clazz = Class.forName(className);
            Class<?> i  = clazz.getInterfaces()[0];
            serviceMapping.put(i.getName(),clazz.newInstance());
        }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
