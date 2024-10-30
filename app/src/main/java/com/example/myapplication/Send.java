package com.example.myapplication;

public class Send {
    static {
        System.loadLibrary("serial_port");// 加载 native 库
    }
    public native int sendData(int fd, byte[]data);
    // 定义帧的起始位和结束位
    private static final byte HEADER = (byte) 0x02;
    private static final byte FOOTER = (byte) 0x03;

    // 封装数据帧
    public static byte[] createFrame(byte commandType, byte[] data) {
        int dataLength = data == null ? 0 : data.length;
        byte[] frame = new byte[4 + dataLength];

        frame[0] = HEADER;
        frame[1] = commandType;
        frame[2] = (byte) dataLength;

        // 复制数据区内容
        if (data != null) {
            System.arraycopy(data, 0, frame, 3, dataLength);
        }

//        // 计算并填入校验和
//        frame[3 + dataLength] = calculateChecksum(frame, 1, 2 + dataLength);
//        frame[4 + dataLength] = FOOTER;

        return frame;
    }

}
