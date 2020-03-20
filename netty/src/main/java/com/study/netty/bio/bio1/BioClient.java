package com.study.netty.bio.bio1;

import lombok.extern.java.Log;

import java.io.*;
import java.net.Socket;

/**
 * @Desc: 阻塞I/O客户端
 * @Author: thy
 * @CreateTime: 2018/11/11
 **/
@Log
public class BioClient {

    private static int DEFAULT_SERVER_PORT = 7777;
    private static String DEFAULT_SERVER_IP = "127.0.0.1";

    public static void send(String message) {
        send(DEFAULT_SERVER_PORT, message);
    }

    public static void send(int port, String message) {
        log.info("客户端发送消息：" + message);

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket(DEFAULT_SERVER_IP, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            log.info("客户端收到消息：" + in.readLine());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in = null;
            }
            if (out != null) {
                out.close();
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
