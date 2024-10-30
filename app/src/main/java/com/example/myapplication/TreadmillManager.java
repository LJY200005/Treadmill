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
            case "setSpeed" : serialComm.sendData(fd,deviceControl.setSpeed((byte) 10)); break;
            case "setIncline" : serialComm.sendData(fd,deviceControl.setIncline((byte) 10)); break;
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

    // 功能描述：
    // 该类综合调用了各个模块的功能，可以作为主程序入口，负责从设备获取信息、获取状态以及控制设备。
}
