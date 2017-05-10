package com.cn.hnust.pojo;


/**
 * Created by Administrator on 2017/5/4.
 */
public class Socket {
    private int port;

    private java.net.Socket socket = null;

    public java.net.Socket getSocket() {
        return socket;
    }

    public void setSocket(java.net.Socket socket) {
        this.socket = socket;
    }


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
