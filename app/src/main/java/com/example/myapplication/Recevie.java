package com.example.myapplication;

public class Recevie {
    static {
        System.loadLibrary("serial_port");// 加载 native 库
    }


    public native int receiveData(int fd, byte[] buffer);
}
