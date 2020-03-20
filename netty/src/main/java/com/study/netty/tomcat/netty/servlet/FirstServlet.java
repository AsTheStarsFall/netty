package com.study.netty.tomcat.netty.servlet;


import com.study.netty.tomcat.netty.http.*;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/8/6 3:52
 **/
public class FirstServlet extends MyServlet {
    @Override
    public void doGet(MyRequest request, MyResponse response) throws Exception{
        this.doPost(request,response);
    }

    @Override
    public void doPost(MyRequest request, MyResponse response) throws Exception{
        response.write("This is FirstServlet");
    }
}
