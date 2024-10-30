package com.example.myapplication;

import android.util.Log;

public class TreadmillManager {
    private DeviceInfo deviceInfo = new DeviceInfo();
    private DeviceStats deviceState = new DeviceStats();
    private DeviceControl deviceControl = new DeviceControl();
    private Serialport serialComm;
    private int fd = -1;

    public TreadmillManager(String portName, int baudRate) {
        serialComm = new Serialport();
        fd = serialComm.openPort(portName,baudRate);
        if(fd != -1){
            //串口打开成功
            System.out.println("===>SerialPort open success!");
        }else {
            System.out.println("===>SerialPort open fail!");
        }
    }

    public void fetchDeviceInfo(String operation) {
        switch (operation){
            case "getModel" : serialComm.sendData(fd,deviceInfo.getDeviceModel()); break;
            case "getSpeed" : serialComm.sendData(fd,deviceInfo.getDeviceSpeedInfo()); break;
            case "getIncline" : serialComm.sendData(fd,deviceInfo.getDeviceInclineInfo()); break;
            case "getPeripheral" : serialComm.sendData(fd,deviceInfo.getPeripheralInfo()); break;
        }
    }

    public void fetchDeviceState() {
        serialComm.sendData(fd,deviceState.getDeviceState());
    }

    public void controlDevice(String operation) {
        switch (operation){
            case "startDevice" : serialComm.sendData(fd,deviceControl.startDevice()); break;
            case "stopDevice" : serialComm.sendData(fd,deviceControl.stopDevice()); break;
            case "pauseDevice" : serialComm.sendData(fd,deviceControl.pauseDevice()); break;
            case "setSpeed" : serialComm.sendData(fd,deviceControl.setSpeed((byte) 1000)); break;
            case "setIncline" : serialComm.sendData(fd,deviceControl.setIncline((byte) 500)); break;
            case "getPeripheral" : serialComm.sendData(fd,deviceInfo.getPeripheralInfo()); break;
        }
    }
    public void closeport(){
        if (fd != -1) {
            // 关闭串口
            serialComm.closePort(fd);
            Log.d("SerialPort", "串口已关闭");
        }
    }

    public int receiveprot(byte[] frame){
        int bytesRead;
        bytesRead = serialComm.receiveData(fd,frame);
        return bytesRead;
    }

    public void Frameanalyze(byte[] frame){
        int command = frame[1];
        int subcommand = frame[2];
        switch (command){
            case 0x50:  //设备信息返回帧
                switch (subcommand){
                    case 0x00:  //获取设备机型

                        break;
                    case 0x02:  //获取速度参数
                        MainActivity.high_speed = frame[3];
                        MainActivity.low_speed = frame[4];
                        MainActivity.speed_flag = frame[5];
                        break;
                    case 0x03:  //获取坡度参数
                        MainActivity.high_incline = frame[3];
                        MainActivity.low_incline = frame[4];
                        break;
                    case 0x05:  //获取外设参数

                        break;
                }
                break;
            case 0x51:
                switch (subcommand){
                    case 0x00:  //待机状态

                        break;
                    case 0x01:  //故障状态

                        break;
                    case 0x02:  //倒计时启动状态

                        break;
                    case 0x03:  //运行中状态
                        MainActivity.speed = frame[3];
                        MainActivity.incline = frame[4];
                        MainActivity.totaltime = (int) ((frame[5] & 0xFF)
                                | ((frame[6] & 0xFF)<<8)
                                | ((frame[7] & 0xFF)<<16)
                                | ((frame[8] & 0xFF)<<24));
                        MainActivity.totalmeter = (int) ((frame[9] & 0xFF)
                                | ((frame[10] & 0xFF)<<8)
                                | ((frame[11] & 0xFF)<<16)
                                | ((frame[12] & 0xFF)<<24));
                        break;
                    case 0x04:  //减速停止中
                        MainActivity.speed = frame[3];
                        MainActivity.incline = frame[4];
                        MainActivity.totaltime = (int) ((frame[5] & 0xFF)
                                | ((frame[6] & 0xFF)<<8)
                                | ((frame[7] & 0xFF)<<16)
                                | ((frame[8] & 0xFF)<<24));
                        MainActivity.totalmeter = (int) ((frame[9] & 0xFF)
                                | ((frame[10] & 0xFF)<<8)
                                | ((frame[11] & 0xFF)<<16)
                                | ((frame[12] & 0xFF)<<24));
                        break;
                    case 0x05:  //暂停状态
                        MainActivity.totaltime = (int) ((frame[3] & 0xFF)
                                | ((frame[4] & 0xFF)<<8)
                                | ((frame[5] & 0xFF)<<16)
                                | ((frame[6] & 0xFF)<<24));
                        break;
                    case 0x06:  //已停机但未回到待机状态

                        break;
                    case 0x20:  //坡度学习中状态

                        break;
                }
                break;
            case 0x53:  //设备信息返回帧
                switch (subcommand){
                    case 0x01:  //开始
                        MainActivity.stratTime = frame[3];
                        break;
                    case 0x03:  //停止

                        break;
                    case 0x04:  //暂停

                        break;
                    case 0x07:  //实际速度
                        MainActivity.speed = frame[3];
                        break;
                    case 0x08:  //实际坡度
                        MainActivity.incline = frame[3];
                        break;
                    case 0x20:  //坡度学习

                        break;
                }
                break;
        }
    }

    // 功能描述：
    // 该类综合调用了各个模块的功能，可以作为主程序入口，负责从设备获取信息、获取状态以及控制设备。
}
