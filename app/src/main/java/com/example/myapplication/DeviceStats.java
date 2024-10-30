package com.example.myapplication;


public class DeviceStats {
    private TreadmillProtocol protocol = new TreadmillProtocol();
    public byte[] getDeviceState() {
        return protocol.constructFrame(TreadmillProtocol.SYS_STATE, new byte[] {});
    }
    // 该模块通过SYS_STATE指令获取设备的当前状态，包括待机、故障、运行、暂停等状态信息。
}
