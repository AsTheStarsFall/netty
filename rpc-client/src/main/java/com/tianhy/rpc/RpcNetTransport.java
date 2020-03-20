package com.tianhy.rpc;

import com.tianhy.request.RpcRequest;

import java.io.*;
import java.net.Socket;

/**
 * {@link}
 *
 * @Desc: 消息传输
 * @Author: thy
 * @CreateTime: 2019/6/19
 **/
public class RpcNetTransport {

    private String host;
    private int port;

    public RpcNetTransport(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public Object send(RpcRequest rpcRequest) {
        Socket socket = null;
        Object result = null;

        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            socket = new Socket(host, port);

            oos = new ObjectOutputStream(socket.getOutputStream());
            //序列化
            oos.writeObject(rpcRequest);
            oos.flush();

            ois = new ObjectInputStream(socket.getInputStream());
            //反序列化
            result = ois.readObject();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(oos!=null){
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(ois!=null){
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
