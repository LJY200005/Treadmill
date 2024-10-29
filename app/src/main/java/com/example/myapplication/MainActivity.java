package com.example.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    public static Button buttonStartPause;
    public static Button buttonFinish;
    public static Button buttonSpeed;
    public static Button buttonIncline;
    public static boolean isRunning = false;
    public static int speed= 0;
    public static int incline = 0;

    public static TextView textViewSpeed;
    public static TextView textViewIncline;
    public static Dialog dialog;
    private Serialport serialPort;
    private int fd = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dialog = new Dialog(this);
        serialPort = new Serialport();

        //串口初始化
        fd = serialPort.openPort("/dev/ttyS2",19200);
        if(fd != -1){
            //串口打开成功
            System.out.println("===>SerialPort open success!");
        }else {
            System.out.println("===>SerialPort open fail!");
        }
        updateUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (fd != -1) {
            // 关闭串口
            serialPort.closePort(fd);
            Log.d("SerialPort", "串口已关闭");
        }
    }
    private void showAdjustDialog(String title, int initialValue) {

        dialog.setContentView(R.layout.dialog_adjust_value);

        TextView dialogTitle = dialog.findViewById(R.id.dialogTitle);
        TextView currentValue = dialog.findViewById(R.id.currentValue);
        Button buttonDecrease = dialog.findViewById(R.id.buttonDecrease);
        Button buttonIncrease = dialog.findViewById(R.id.buttonIncrease);
        Button presetValue1 = dialog.findViewById(R.id.presetValue1);
        Button presetValue2 = dialog.findViewById(R.id.presetValue2);
        Button presetValue3 = dialog.findViewById(R.id.presetValue3);

        dialogTitle.setText("调整" + title);
        currentValue.setText(String.valueOf(initialValue));

        // 增减按钮的点击事件
        buttonIncrease.setOnClickListener(v -> {
            int value = Integer.parseInt(currentValue.getText().toString());
            if(value < 20)
                currentValue.setText(String.valueOf(++value));
        });

        buttonDecrease.setOnClickListener(v -> {
            int value = Integer.parseInt(currentValue.getText().toString());
            if (value > 0) {  // 确保不会减少到负值
                currentValue.setText(String.valueOf(--value));
            }
        });

        // 预设值按钮点击事件
        View.OnClickListener presetClickListener = v -> {
            int value = Integer.parseInt(((Button) v).getText().toString());
            currentValue.setText(String.valueOf(value));
        };
        presetValue1.setOnClickListener(presetClickListener);
        presetValue2.setOnClickListener(presetClickListener);
        presetValue3.setOnClickListener(presetClickListener);

        // 调用回调，将新值传递给 MainActivity
        dialog.findViewById(R.id.buttonConfirm).setOnClickListener(v -> {
            int newValue = Integer.parseInt(currentValue.getText().toString());
            // 调用回调，将新值传递给 MainActivity
            onValueChanged(title,newValue);

            dialog.dismiss();
        });

        dialog.show();
    }

    public void updateUI(){
        textViewSpeed = findViewById(R.id.textViewSpeed);
        textViewIncline = findViewById(R.id.textViewIncline);
        buttonStartPause = findViewById(R.id.buttonStartPause);
        buttonFinish = findViewById(R.id.buttonFinish);
        buttonSpeed = findViewById(R.id.buttonSpeed);
        buttonIncline = findViewById(R.id.buttonIncline);

        // 设置 Start/Pause 按钮的点击事件
        buttonStartPause.setOnClickListener(v -> {
            if (MainActivity.isRunning) {
                Status.pause(); // 如果正在运行，点击按钮会暂停
            } else {
                Status.start(); // 如果未运行，点击按钮会开始
            }
        });

        buttonFinish.setOnClickListener(v -> {
            onValueChanged("速度",0);
            onValueChanged("坡度",0);
            Status.finishOperation();
        });

        // 读取保存的速度和坡度值
        SharedPreferences sharedPreferences = getSharedPreferences("TreadmillPrefs", MODE_PRIVATE);
        speed = sharedPreferences.getInt("speed", 0);    // 读取速度，默认值为 0
        incline = sharedPreferences.getInt("incline", 0); // 读取坡度，默认值为 0

        updateSpeedDisplay();
        updateInclineDisplay();

        // 设置速度按钮点击事件
        MainActivity.buttonSpeed.setOnClickListener(v -> showAdjustDialog("速度", MainActivity.speed));
        // 设置坡度按钮点击事件
        MainActivity.buttonIncline.setOnClickListener(v -> showAdjustDialog("坡度", MainActivity.incline));
    }

    public void onValueChanged(String title, int newValue){
        Context context = null;
        if (title.equals("速度")) {
            MainActivity.speed = newValue;
            updateSpeedDisplay();
        } else if (title.equals("坡度")) {
            MainActivity.incline = newValue;
            updateInclineDisplay();
        }

        // 如果需要保存新值到本地，可以使用 SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("TreadmillPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("speed", MainActivity.speed);
        editor.putInt("incline", MainActivity.incline);
        editor.apply();
    }
    // 更新速度显示
    public  static void updateSpeedDisplay() {
        MainActivity.textViewSpeed.setText("当前速度: " + MainActivity.speed + "Km/h");
    }

    // 更新坡度显示
    public static void updateInclineDisplay() {
        MainActivity.textViewIncline.setText("当前坡度: " + MainActivity.incline);
    }
}
