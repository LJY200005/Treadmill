package com.example.myapplication;

public class DeviceInfo {

    // 协议指令
    public static final byte INFO_MODEL = 0x00; // 获取设备机型
    public static final byte INFO_SPEED = 0x02; // 获取速度参数
    public static final byte INFO_INCLINE = 0x03; // 获取坡度参数
    public static final byte INFO_PERIPHERAL = 0x05; // 获取外设参数

    private TreadmillProtocol protocol = new TreadmillProtocol();
    public byte[] getDeviceModel() {
        return protocol.constructFrame(TreadmillProtocol.SYS_INFO, new byte[] {INFO_MODEL});
    }

    public byte[] getDeviceSpeedInfo() {
        return protocol.constructFrame(TreadmillProtocol.SYS_INFO, new byte[] {INFO_SPEED});
    }

    public byte[] getDeviceInclineInfo() {
        return protocol.constructFrame(TreadmillProtocol.SYS_INFO, new byte[] {INFO_INCLINE});
    }

    public byte[] getPeripheralInfo() {
        return protocol.constructFrame(TreadmillProtocol.SYS_INFO, new byte[] {INFO_PERIPHERAL});
    }
}
