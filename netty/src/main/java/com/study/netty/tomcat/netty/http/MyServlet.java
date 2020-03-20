package com.study.netty.tomcat.netty.http;


/**
 * {@link}
 *
 * @Desc: servlet封装
 * @Author: thy
 * @CreateTime: 2019/8/6 3:39
 **/
public abstract class MyServlet {


    public void service(MyRequest request,MyResponse response)throws Exception{
        if("GET".equalsIgnoreCase(request.getMethod())){
            doGet(request,response);
        }else {
            doPost(request,response);
        }
    }

    public abstract void doGet(MyRequest request,MyResponse response) throws Exception;
    public abstract void doPost(MyRequest request,MyResponse response) throws Exception;

}
