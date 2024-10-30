package com.example.myapplication;

public class TreadmillProtocol {
    // 常量定义
    private static final byte START_BYTE = 0x02; // 起始码
    private static final byte END_BYTE = 0x03; // 终止码

    // 协议指令
    public static final byte SYS_INFO = 0x50; // 设备信息指令
    public static final byte SYS_STATE = 0x51; // 设备状态指令
    public static final byte SYS_CONTROL = 0x53; // 设备控制指令

    // 功能描述：
    // 该类提供帧的起始码、终止码和一些协议中通用的功能，包括校验码计算和帧格式构造
    private byte calculateChecksum(byte command,byte[] data) {       //此处为异或
        byte checksum = command;
        for (int i = 0; i < data.length; i++) {
            checksum ^= data[i];
        }
        return checksum;
    }

    // 构造数据帧
    public byte[] constructFrame(byte command, byte[] data) {
        int dataLength = (data != null) ? data.length : 0;
        // 帧长度：起始码 + 指令码 + 数据码（可变长） + 校验码 + 终止码
        byte[] frame = new byte[3 + dataLength + 1];
        int index = 0;
        frame[index++] = START_BYTE;
        frame[index++] = command;
        if(data != null){
            System.arraycopy(data, 0, frame, index, data.length);
            index += data.length;
        }
        byte checkcode = calculateChecksum(command, data);
        frame[index++] = checkcode;
        frame[index] = END_BYTE;

        return frame;
    }
}
