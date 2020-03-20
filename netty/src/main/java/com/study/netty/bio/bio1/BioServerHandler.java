package com.study.netty.bio.bio1;

import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;

/**
 * @Desc: 消息处理
 * @Author: thy
 * @CreateTime: 2018/11/11
 **/
@Log
public class BioServerHandler implements Runnable {

    private Socket socket;

    public BioServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            //从socket接收到流
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            String message;
            String replay="服务端自动回复...";

            //因为是一个线程，需要运行
            while (true) {
                if ((message = in.readLine()) == null) break;
                log.info("服务端收到消息：" + message);
                out.println(replay);
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.warning(e.getLocalizedMessage());
        } finally {

            //释放资源，关闭流
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                in = null;
            }

            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                out = null;
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                socket = null;
            }
        }
    }
}
