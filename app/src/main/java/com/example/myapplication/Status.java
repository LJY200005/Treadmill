package com.example.myapplication;

import android.widget.Button;

public class Status {

    private int incline;
    private int speed;
    // 开始操作
    public static void start() {
        MainActivity.isRunning = true;
        MainActivity.buttonStartPause.setText("Pause"); // 修改按钮文字为 Pause
        // TODO: 开始相关的逻辑处理，例如计时或其他操作
    }

    // 暂停操作
    public static void pause() {
        MainActivity.isRunning = false;
        MainActivity.buttonStartPause.setText("Start"); // 修改按钮文字为 Start
        // TODO: 暂停相关的逻辑处理
    }

    // 完成操作，重置状态
    public static void finishOperation() {
        MainActivity.isRunning = false;
        MainActivity.buttonStartPause.setText("Start"); // 重置按钮文字为 Start
        // TODO: 结束操作的逻辑处理，例如重置计时或其他状态
    }
}
