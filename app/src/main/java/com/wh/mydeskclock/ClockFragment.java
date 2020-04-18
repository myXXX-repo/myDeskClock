package com.wh.mydeskclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static java.lang.Thread.sleep;


public class ClockFragment extends Fragment {
    private String TAG = "WH_ClockFragment";

    private MainActivity mParent;

    private BroadcastReceiver timeRecv = null;
    private IntentFilter timeFilter = null;

    private TextView tv_hour, tv_min, tv_week;
    private ConstraintLayout constraintLayout = null;

    private int BackGroundColor = Color.WHITE;

    private int screenBrightness = 0;


    public ClockFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mParent = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        timeRecv = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setTime();
            }
        };
        timeFilter = new IntentFilter(Intent.ACTION_TIME_TICK);
        mParent.registerReceiver(timeRecv, timeFilter);
    }

    @Override
    public void onStart() {
        super.onStart();
        setWindowSystemUiVisibility();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mParent.unregisterReceiver(timeRecv);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_clock, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_hour = getView().findViewById(R.id.tv_hour);
        tv_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });

        tv_min = getView().findViewById(R.id.tv_min);
        tv_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDarkMode();
            }
        });

        tv_week = getView().findViewById(R.id.tv_week);
        tv_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLight();
            }
        });
        toggleDarkMode_tv(bw2wb(BackGroundColor));

        constraintLayout = getView().findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(BackGroundColor);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashEInkScreen();
            }
        });
//        constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                showOptDialog(mParent);
//                return true;
//            }
//        });

        setTime();
    }

    // 构建并显示弹窗
    private void showOptDialog(Context context) {
        final String[] items = {
                "Toggle Dark Mode",
                "Toggle Background Light",
                "Refresh Time",
                "Flash EInkScreen"};
        //                "set OR"

        AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
        listDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setWindowSystemUiVisibility();
            }
        });
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, String.valueOf(which));
                switch (which) {
                    case 0: {// dark mode
                        toggleDarkMode();
                        break;
                    }
                    case 1: {// light
                        toggleLight();
                        break;
                    }
                    case 2: {// refresh time
                        tv_hour.callOnClick();
                        break;
                    }
                    case 3: {
                        flashEInkScreen();
                        break;
                    }
                    case 4: {
                        break;
                    }
                }
                setWindowSystemUiVisibility();
            }
        });
        listDialog.show();
    }

    // 刷新屏幕
    private void flashEInkScreen() {
        new Thread("flash screen"){
            @Override
            public void run(){
                toggleDarkMode();
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                toggleDarkMode();
            }
        }.start();
    }

    // 设置窗口系统显示 主要用于全屏--隐藏标题栏 状态栏 动作栏
    private void setWindowSystemUiVisibility() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mParent.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    // 开关黑色背景
    private void toggleDarkMode() {
        int TextColor = BackGroundColor;
        BackGroundColor = bw2wb(BackGroundColor);
        constraintLayout.setBackgroundColor(BackGroundColor);
        toggleDarkMode_tv(TextColor);
    }

    private void toggleDarkMode_tv(int TextColor){
        tv_hour.setTextColor(TextColor);
        tv_min.setTextColor(TextColor);
        tv_week.setTextColor(TextColor);
    }

    // 开关背光灯
    private void toggleLight() {
        String msg = null;
        if (screenBrightness == 1) {
            screenBrightness = 0;
            msg = "light off";
        } else {
            screenBrightness = 1;
            msg = "light on";
        }
        setWindowBrightness(screenBrightness);
        Toast.makeText(mParent,msg,Toast.LENGTH_SHORT).show();
    }

    // 设置窗口屏幕亮度
    private void setWindowBrightness(int brightness) {
        Window window = mParent.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

    // 黑色白色互相转换
    private int bw2wb(int color) {
        switch (color) {
            case Color.WHITE: {
                return Color.BLACK;
            }
            case Color.BLACK: {
                return Color.WHITE;
            }
            default: {
                return color;
            }
        }
    }

    // 获取并更新UI时间
    private void setTime() {
        Log.d(TAG, "onSetTime");

        Calendar calendar = Calendar.getInstance();

        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        tv_week.setText(String.format("星期%s", num2Chinese(weekday)));

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        tv_hour.setText(ensure2Numbers(hour));

        int min = calendar.get(Calendar.MINUTE);
        tv_min.setText(ensure2Numbers(min));
    }

    // 补全两位数字 for 分钟
    private String ensure2Numbers(int num) {
        if (num >= 0 && num < 10) {
            return "0" + num;
        } else {
            return String.valueOf(num);
        }
    }

    // 数字转汉字 for 星期
    private String num2Chinese(int num) {
        String[] c = {"六", "日", "一", "二", "三", "四", "五"};

        if (num > -1 && num < c.length) {
            return c[num];
        } else {
            return c[0];
        }
    }
}
