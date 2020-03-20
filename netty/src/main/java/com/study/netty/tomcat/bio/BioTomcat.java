package com.study.netty.tomcat.bio;

import com.study.netty.tomcat.bio.http.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;

/**
 * {@link}
 *
 * @Desc: BIO Tomcat
 * @Author: thy
 * @CreateTime: 2019/8/6 3:34
 **/
public class BioTomcat {

    //1、配置默认端口8080,ServerSocket,ip:localhost
    private int port = 8080;
    private ServerSocket serverSocket;
    //url-pattern 和 Servlet映射
    private HashMap<String, MyServlet> servletMapping = new HashMap<String, MyServlet>();
    //配置文件
    private Properties webXml = new Properties();

    /**
     * 2、配置web.xml,封装的servlet继承HttpServlet
     * servlet-name
     * servlet-class
     * url-pattern
     */


    //初始化
    public void init() {
        //3、读取配置文件，url-pattern与servlet建立映射关系
        try {
            String path = this.getClass().getResource("/").getPath();
            FileInputStream fis = new FileInputStream(path+ "web.properties");

            webXml.load(fis);

            for (Object k : webXml.keySet()) {
                String key = k.toString();
                if(key.endsWith(".url")){
                    String servletName = key.replaceAll("\\.url$", "");
                    String url = webXml.getProperty(key);
                    String className = webXml.getProperty(servletName + ".className");
                    MyServlet servlet = (MyServlet)Class.forName(className).newInstance();
                    servletMapping.put(url,servlet);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start(){
        init();

        try {
            serverSocket = new ServerSocket(this.port);
            System.out.println("BioTomcat 已经启动，端口号："+ this.port);

            //自旋处理客户端请求
            while (true){
                Socket socket = serverSocket.accept();
                process(socket);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void process(Socket socket) throws Exception{
        //4、处理HTTP请求内容

        //从socket获取到输入流、输出流
        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        //封装
        MyRequest request = new MyRequest(is);
        MyResponse response = new MyResponse(os);

        //5、获取到URL，将对应的servlet实例化
        String url = request.getUrl();
        if(servletMapping.containsKey(url)){
            //6、调用实例化对象的service()
            servletMapping.get(url).service(request,response);
        }else{
            response.write("404 Not Found!");
        }

        os.flush();
        os.close();
        is.close();
        socket.close();
    }
}
