package com.wh.mydeskclock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mySync.SharedPreferenceUtils;
import com.wh.mydeskclock.Widget.MyDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Calendar;

import static android.content.Context.BATTERY_SERVICE;
import static java.lang.Thread.sleep;


public class MainClockFragment extends Fragment {
    private String TAG = "WH_ClockFragment";

    private MainActivity mParent;

    private SharedPreferenceUtils sharedPreferenceUtils = null;
    private BroadcastReceiver timeReceiver = null;
    private BroadcastReceiver batteryReceiver = null;

    private BatteryManager batteryManager = null;

    private TextView tv_hour, tv_min, tv_week, tv_battery;
    private ConstraintLayout constraintLayout = null;
    private ProgressBar pb_battery;


    private int lastBatteryLevel = -1;

    private int STATUS_SCREEN_OR;
//    private int STATUS_SCREEN_OR = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    private int STATUS_SCREEN_BRIGHTNESS = 0;
    private int STATUS_BACKGROUND_COLOR = Color.WHITE;


    private int COAST_BATTERY = 0;
    private int COAST_MINUTE = 0;
    private int COAST_FLASH = 0;
    private int COAST_SWITCH_THEME = 0;
    private int COAST_SWITCH_LIGHT = 0;


//    private boolean SETTING_ENABLE_NIGHT_MODE_AUTO_SWITCH = false;
//
//
//    private static final int TIME_DAY_HOUR = 6;
//    private static final int TIME_NIGHT_HOUR = 22;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mParent = (MainActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clock, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mParent.unregisterReceiver(timeReceiver);
        mParent.unregisterReceiver(batteryReceiver);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferenceUtils = initPreference(mParent);
        STATUS_SCREEN_OR = requireActivity().getSharedPreferences(SharedPreferenceUtils.SharedPreferenceFile,Context.MODE_PRIVATE).getInt("setting_screen_or",ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        timeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setTime();
                COAST_MINUTE += 1;
                sharedPreferenceUtils.setCOAST_MINUTE(COAST_MINUTE);
            }
        };
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        mParent.registerReceiver(timeReceiver, timeFilter);

        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int currentBatteryLevel = intent.getIntExtra("level", -1);
                if (currentBatteryLevel == -1) {
                    Log.e(TAG, "can't get battery level");
                } else {
                    if (lastBatteryLevel == -1) {
                        lastBatteryLevel = currentBatteryLevel;
                    } else {
                        if (lastBatteryLevel > currentBatteryLevel) {
                            COAST_BATTERY += (lastBatteryLevel - currentBatteryLevel);
                            sharedPreferenceUtils.setCOAST_BATTERY(COAST_BATTERY);
                            lastBatteryLevel = currentBatteryLevel;
                        }
                    }
                    setBattery(currentBatteryLevel);
                }
            }
        };
        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mParent.registerReceiver(batteryReceiver, batteryFilter);

        batteryManager = (BatteryManager) mParent.getSystemService(BATTERY_SERVICE);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        View view = getView();
        if (view == null) {
            return;
        }

        tv_hour = view.findViewById(R.id.tv_hour);
        tv_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTime();
            }
        });

        tv_min = view.findViewById(R.id.tv_min);
        tv_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDarkMode(true);
            }
        });

        tv_week = view.findViewById(R.id.tv_week);
        tv_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleLight();
            }
        });

        tv_battery = view.findViewById(R.id.tv_battery);
        tv_battery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBattery();
            }
        });
        tv_battery.setOnLongClickListener(new View.OnLongClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public boolean onLongClick(View v) {
                changeOR(mParent);
                return true;
            }
        });

        Utils.toggleDarkMode_tv(
                tv_hour, tv_min, tv_week, tv_battery, Utils.b2w2b(STATUS_BACKGROUND_COLOR));

        constraintLayout = view.findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(STATUS_BACKGROUND_COLOR);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flashEInkScreen();
            }
        });
        constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showOptDialog(mParent);
                return true;
            }
        });

        pb_battery = view.findViewById(R.id.progressBar);
        pb_battery.setVisibility(View.GONE);


        initScreen();


        new Thread() {
            @Override
            public void run() {

                try {
                    Document document = Jsoup.connect("https://www.coolapk.com/apk/260552").get();
                    String title = document.title();
                    String newestVersionName = title.split(" ")[2];
                    String currentVersionName = Utils.getAppVersionName(mParent);
                    if (newestVersionName.equals(currentVersionName)) {
                        Log.d(TAG, "app is the newest version");
                    } else {
                        Log.d(TAG, "currentVersion:" + currentVersionName + " newestVersion:" + newestVersionName);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void initScreen() {
        setTime();
        setBattery();
        setOR(mParent, STATUS_SCREEN_OR);
        Utils.setWindowBrightness(mParent, STATUS_SCREEN_BRIGHTNESS);

    }

    // 刷新屏幕
    private void flashEInkScreen() {
        new Thread("flash screen") {
            @Override
            public void run() {
                toggleDarkMode(false);
                try {
                    sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                toggleDarkMode(false);
            }
        }.start();
        // 计数
        COAST_FLASH += 1;
        sharedPreferenceUtils.setCOAST_FLASH(COAST_FLASH);
    }

    // 开关背光灯
    private void toggleLight() {
        String msg;
        if (STATUS_SCREEN_BRIGHTNESS == 1) {
            STATUS_SCREEN_BRIGHTNESS = 0;
            msg = "light off";
        } else {
            STATUS_SCREEN_BRIGHTNESS = 1;
            msg = "light on";
        }
        Utils.setWindowBrightness(mParent, STATUS_SCREEN_BRIGHTNESS);
        Toast.makeText(mParent, msg, Toast.LENGTH_SHORT).show();
        // 计数
        COAST_SWITCH_LIGHT += 1;
        sharedPreferenceUtils.setCOAST_SWITCH_LIGHT(COAST_SWITCH_LIGHT);
        sharedPreferenceUtils.setSTATUS_SCREEN_BRIGHTNESS(STATUS_SCREEN_BRIGHTNESS);
    }

    // 开关黑色背景
    private void toggleDarkMode(boolean Count) {
        int TextColor = STATUS_BACKGROUND_COLOR;
        STATUS_BACKGROUND_COLOR = Utils.b2w2b(STATUS_BACKGROUND_COLOR);
        constraintLayout.setBackgroundColor(STATUS_BACKGROUND_COLOR);
        Utils.toggleDarkMode_tv(tv_hour, tv_min, tv_week, tv_battery, TextColor);
        // 计数
        if (Count) {
            COAST_SWITCH_THEME += 1;
            sharedPreferenceUtils.setCOAST_SWITCH_THEME(COAST_SWITCH_THEME);
        }
    }

    // 获取并更新UI时间
    private void setTime() {
        Calendar calendar = Calendar.getInstance();

        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        tv_week.setText(String.format("星期%s", Utils.num2Chinese(weekday)));
        tv_hour.setText(Utils.ensure2Numbers(hour));
        tv_min.setText(Utils.ensure2Numbers(min));

//        if (SETTING_ENABLE_NIGHT_MODE_AUTO_SWITCH) {
//            autoTask(hour, min);
//        }

//        autoFlashScreen();
    }

    // 定时自动任务
//    private void autoTask(int hour, int min) {
        // 自动切换 dark mode
//        if (hour > TIME_DAY_HOUR && hour < TIME_NIGHT_HOUR) { // day
//
//            // 自动关闭 Dark Mode
//            if (STATUS_BACKGROUND_COLOR != Color.WHITE) {
//                toggleDarkMode(true);
//            }
//
//            // 自动关闭 背光灯
//            if (STATUS_SCREEN_BRIGHTNESS != 0) {
//                toggleLight();
//            }
//        } else {
//
//            // 自动开启 Dark Mode
//            if (STATUS_BACKGROUND_COLOR != Color.BLACK) {
//                toggleDarkMode(true);
//            }
//
//            // 自动开启背光灯
//            if (STATUS_SCREEN_BRIGHTNESS != 1) {
//                toggleLight();
//            }
//        }
//    }

    // 自动刷新屏幕
//    private void autoFlashScreen() {
//        if (COAST_MINUTE != 0 && COAST_MINUTE % 100 == 0) {
//            flashEInkScreen();
//        }
//    }

    // 获取并更新UI电池
    @SuppressLint("SetTextI18n")
    private void setBattery() {
        int batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        lastBatteryLevel = batteryLevel;
        tv_battery.setText(batteryLevel + "%");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pb_battery.setProgress(batteryLevel, false);
        } else {
            pb_battery.setProgress(batteryLevel);
        }
    }

    @SuppressLint("SetTextI18n")
    private void setBattery(int level) {
        tv_battery.setText(level + "%");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pb_battery.setProgress(level, false);
        } else {
            pb_battery.setProgress(level);
        }
    }

    // 切换屏幕方向
    @SuppressLint("SourceLockedOrientationActivity")
    private void changeOR(Activity activity) {
        if (STATUS_SCREEN_OR == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            STATUS_SCREEN_OR = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
            setOR(activity, STATUS_SCREEN_OR);
        } else {
            STATUS_SCREEN_OR = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
            setOR(activity, STATUS_SCREEN_OR);
        }
        sharedPreferenceUtils.setSTATUS_SCREEN_OR(STATUS_SCREEN_OR);
    }

    private void setOR(Activity activity, int OR) {
        activity.setRequestedOrientation(OR);
    }

    // 构建并显示弹窗
    private void showOptDialog(Context context) {
        final String[] items = {
                "已经切换背景" + COAST_SWITCH_THEME+"次",
                "开关背光灯" + COAST_SWITCH_LIGHT+"次",
                "屏幕刷新" + COAST_FLASH+"次",
                "已经消耗电量" + COAST_BATTERY+"%",
                "myDeskClock已陪伴你" + COAST_MINUTE +"分钟",
        };

        AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
        listDialog.setPositiveButton("好的，知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        listDialog.setItems(items,null);
        MyDialog myDialog = new MyDialog(listDialog);
        myDialog.setFullScreen();
        myDialog.setGRAVITY(Gravity.BOTTOM);
        myDialog.show(mParent.getSupportFragmentManager(), "opt");


    }

    private SharedPreferenceUtils initPreference(Context context) {
        SharedPreferenceUtils sharedPreferenceUtils = new SharedPreferenceUtils(context);
        COAST_BATTERY = sharedPreferenceUtils.getCOAST_BATTERY();
        COAST_MINUTE = sharedPreferenceUtils.getCOAST_MINUTE();
        COAST_FLASH = sharedPreferenceUtils.getCOAST_FLASH();
        COAST_SWITCH_THEME = sharedPreferenceUtils.getCOAST_SWITCH_THEME();
        COAST_SWITCH_LIGHT = sharedPreferenceUtils.getCOAST_SWITCH_LIGHT();
        STATUS_SCREEN_OR = sharedPreferenceUtils.getSTATUS_SCREEN_OR();
        STATUS_SCREEN_BRIGHTNESS = sharedPreferenceUtils.getSTATUS_SCREEN_BRIGHTNESS();
        return sharedPreferenceUtils;
    }

}
// 添加设置界面
// 开关 更新时间的同时自动刷新屏幕
// 数值 自定义自动刷新的频率

// 开关 自动切换夜间模式 开启后取消tv的点击事件

// 切换显示电池电量状态

// 长按背景弹窗内容 设置 统计
// 统计界面显示各个统计的数值

// 统计信息更新后自动将各个数值保存

// 横屏模式
// 横屏显示todo
