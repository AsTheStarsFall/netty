package com.study.netty.bio.bio2;

import java.io.*;
import java.net.Socket;

/**
 * {@link}
 *
 * @Desc:
 * @Author: thy
 * @CreateTime: 2019/5/25
 **/
public class Bio2Client {

    private static final int DEFAULT_PORT = 6666;
    private static final String SERVER_IP = "127.0.0.1";

    public static void send(String message) {
        send(DEFAULT_PORT, message);
    }

    private static void send(int defaultPort, String message) {
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket(SERVER_IP, defaultPort);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            out.println(message);
            System.out.println("Send message to server success");

            String respStr = in.readLine();

            System.out.println("Receive server message :" + respStr);
        } catch (Exception e) {
            //none
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
