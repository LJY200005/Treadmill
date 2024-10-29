package com.example.myapplication;

public class Serialport {
    static {
        System.loadLibrary("serial_port");// 加载 native 库
    }

    public native int openPort(String portName, int buadRate);
    public native int sendData(int fd, byte[]data);
    public native int receiveData(int fd, byte[] buffer);
    public native int closePort(int fd);
}
