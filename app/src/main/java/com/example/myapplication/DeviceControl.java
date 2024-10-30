package com.example.myapplication;

public class DeviceControl {
    // 协议指令
    public static final byte CONTROL_START = 0x01; // 获取设备机型
    public static final byte CONTROL_STOP = 0x03; // 获取速度参数
    public static final byte CONTROL_PAUSE = 0x04; // 获取坡度参数
    public static final byte CONTROL_SPEED = 0x07; // 获取外设参数
    public static final byte CONTROL_INCLINE = 0x08; // 获取外设参数


    private TreadmillProtocol protocol = new TreadmillProtocol();

    public byte[] startDevice() {
        return protocol.constructFrame(TreadmillProtocol.SYS_CONTROL, new byte[] {CONTROL_START});
    }

    public byte[] stopDevice() {
        return protocol.constructFrame(TreadmillProtocol.SYS_CONTROL, new byte[] {CONTROL_STOP});
    }

    public byte[] pauseDevice() {
        return protocol.constructFrame(TreadmillProtocol.SYS_CONTROL, new byte[] {CONTROL_PAUSE});
    }

    public byte[] setSpeed(byte targetSpeed) {
        return protocol.constructFrame(TreadmillProtocol.SYS_CONTROL, new byte[] {CONTROL_SPEED, targetSpeed});
    }

    public byte[] setIncline(byte targetIncline) {
        return protocol.constructFrame(TreadmillProtocol.SYS_CONTROL, new byte[] {CONTROL_INCLINE, targetIncline});
    }
}
