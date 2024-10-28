package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.app.Dialog;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private Button buttonStartPause;
    private Button buttonFinish;
    private int speed= 0;
    private int incline = 0;
    private boolean isRunning = false;

    private TextView textViewSpeed;
    private TextView textViewIncline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        textViewSpeed = findViewById(R.id.textViewSpeed);
        textViewIncline = findViewById(R.id.textViewIncline);

        buttonStartPause = findViewById(R.id.buttonStartPause);
        buttonFinish = findViewById(R.id.buttonFinish);

        // 设置 Start/Pause 按钮的点击事件
        buttonStartPause.setOnClickListener(v -> {
            if (isRunning) {
                pause(); // 如果正在运行，点击按钮会暂停
            } else {
                start(); // 如果未运行，点击按钮会开始
            }
        });

        // 设置 Finish 按钮的点击事件
        buttonFinish.setOnClickListener(v -> finishOperation());

        // 读取保存的速度和坡度值
        SharedPreferences sharedPreferences = getSharedPreferences("TreadmillPrefs", MODE_PRIVATE);
        speed = sharedPreferences.getInt("speed", 0);    // 读取速度，默认值为 0
        incline = sharedPreferences.getInt("incline", 0); // 读取坡度，默认值为 0

        updateSpeedDisplay();
        updateInclineDisplay();

        Button buttonSpeed = findViewById(R.id.buttonSpeed);
        Button buttonIncline = findViewById(R.id.buttonIncline);

        // 设置速度按钮点击事件
        buttonSpeed.setOnClickListener(v -> showAdjustDialog("速度", speed));
        // 设置坡度按钮点击事件
        buttonIncline.setOnClickListener(v -> showAdjustDialog("坡度", incline));


    }

    // 开始操作
    private void start() {
        isRunning = true;
        buttonStartPause.setText("Pause"); // 修改按钮文字为 Pause
        // TODO: 开始相关的逻辑处理，例如计时或其他操作
    }

    // 暂停操作
    private void pause() {
        isRunning = false;
        buttonStartPause.setText("Start"); // 修改按钮文字为 Start
        // TODO: 暂停相关的逻辑处理
    }

    // 完成操作，重置状态
    private void finishOperation() {
        isRunning = false;
        buttonStartPause.setText("Start"); // 重置按钮文字为 Start
        // TODO: 结束操作的逻辑处理，例如重置计时或其他状态
    }
    private void showAdjustDialog(String title, int initialValue) {
        Dialog dialog = new Dialog(this);
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

    public void onValueChanged(String title,int newValue){
        if (title.equals("速度")) {
            speed = newValue;
            updateSpeedDisplay();
        } else if (title.equals("坡度")) {
            incline = newValue;
            updateInclineDisplay();
        }

        // 如果需要保存新值到本地，可以使用 SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("TreadmillPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("speed", speed);
        editor.putInt("incline", incline);
        editor.apply();


    }
    // 更新速度显示
    private void updateSpeedDisplay() {
        textViewSpeed.setText("当前速度: " + speed + "Km/h");
    }

    // 更新坡度显示
    private void updateInclineDisplay() {
        textViewIncline.setText("当前坡度: " + incline);
    }

}
