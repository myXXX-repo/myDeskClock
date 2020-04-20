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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import static android.content.Context.BATTERY_SERVICE;
import static java.lang.Thread.sleep;


public class ClockFragment extends Fragment {
    private String TAG = "WH_ClockFragment";

    private MainActivity mParent;

    private SharedPreference sharedPreference = null;

    private BroadcastReceiver timeReceiver = null;
    private BroadcastReceiver batteryReceiver = null;

    private BatteryManager batteryManager = null;

    private TextView tv_hour, tv_min, tv_week, tv_battery;
    private ConstraintLayout constraintLayout = null;
    private ProgressBar pb_battery;

    private int BackGroundColor = Color.WHITE;

    private int screenBrightness = 0;
    private int lastBatteryLevel = -1;

    private int CurrentScreenOR = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;

    private int COAST_BATTERY = 0;
    private int COAST_MINUTE = 0;
    private int COAST_FLASH = 0;
    private int COAST_SWITCH_THEME = 0;
    private int COAST_SWITCH_LIGHT = 0;

    private boolean SETTING_ENABLE_NIGHT_MODE_AUTO_SWITCH = false;

    private static final int TIME_DAY_HOUR = 6;
    private static final int TIME_NIGHT_HOUR = 22;


    public ClockFragment() {
    }

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

        sharedPreference = initPreference(mParent);

        timeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                setTime();
                COAST_MINUTE += 1;
                sharedPreference.setCOAST_MINUTE(COAST_MINUTE);
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
                            sharedPreference.setCOAST_BATTERY(COAST_BATTERY);
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
                tv_hour, tv_min, tv_week, tv_battery, Utils.b2w2b(BackGroundColor));

        constraintLayout = view.findViewById(R.id.constraintLayout);
        constraintLayout.setBackgroundColor(BackGroundColor);
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

        setTime();
        setBattery();

        changeOR(mParent);
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
        sharedPreference.setCOAST_FLASH(COAST_FLASH);
    }

    // 开关背光灯
    private void toggleLight() {
        String msg;
        if (screenBrightness == 1) {
            screenBrightness = 0;
            msg = "light off";
        } else {
            screenBrightness = 1;
            msg = "light on";
        }
        Utils.setWindowBrightness(mParent, screenBrightness);
        Toast.makeText(mParent, msg, Toast.LENGTH_SHORT).show();
        // 计数
        COAST_SWITCH_LIGHT += 1;
        sharedPreference.setCOAST_SWITCH_LIGHT(COAST_SWITCH_LIGHT);
    }

    // 开关黑色背景
    private void toggleDarkMode(boolean Count) {
        int TextColor = BackGroundColor;
        BackGroundColor = Utils.b2w2b(BackGroundColor);
        constraintLayout.setBackgroundColor(BackGroundColor);
        Utils.toggleDarkMode_tv(tv_hour, tv_min, tv_week, tv_battery, TextColor);
        // 计数
        if (Count) {
            COAST_SWITCH_THEME += 1;
            sharedPreference.setCOAST_SWITCH_THEME(COAST_SWITCH_THEME);
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

        if(SETTING_ENABLE_NIGHT_MODE_AUTO_SWITCH){
            autoTask(hour,min);
        }

//        autoFlashScreen();
    }

    // 定时自动任务
    private void autoTask(int hour,int min){
        // 自动切换 dark mode
        if (hour > TIME_DAY_HOUR && hour < TIME_NIGHT_HOUR) { // day

            // 自动关闭 Dark Mode
            if (BackGroundColor != Color.WHITE) {
                toggleDarkMode(true);
            }

            // 自动关闭 背光灯
            if (screenBrightness != 0) {
                toggleLight();
            }
        } else {

            // 自动开启 Dark Mode
            if (BackGroundColor != Color.BLACK) {
                toggleDarkMode(true);
            }

            // 自动开启背光灯
            if (screenBrightness != 1) {
                toggleLight();
            }
        }
    }

    // 自动刷新屏幕
    private void autoFlashScreen(){
        if(COAST_MINUTE>100 && COAST_MINUTE % 101 == 0){
            flashEInkScreen();
        }
    }

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
    private void changeOR(Activity activity){
        if (CurrentScreenOR == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            CurrentScreenOR = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            CurrentScreenOR = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        }
    }

    // 构建并显示弹窗
    private void showOptDialog(Context context) {
        final String[] items = {
                "COAST_SWITCH_THEME " + COAST_SWITCH_THEME,
                "COAST_SWITCH_LIGHT " + COAST_SWITCH_LIGHT,
                "COAST_MINUTE " + COAST_MINUTE,
                "COAST_FLASH " + COAST_FLASH,
                "COAST_BATTERY " + COAST_BATTERY,
                "Setting",
                "or"
        };

        AlertDialog.Builder listDialog = new AlertDialog.Builder(context);
        listDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                Utils.setWindowSystemUiVisibility(mParent);
            }
        });
        listDialog.setItems(items, new DialogInterface.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d(TAG, String.valueOf(which));
                switch (which) {
                    case 0: {// dark mode
                        toggleDarkMode(true);
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
                    case 6: {
                        if (CurrentScreenOR == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
                            mParent.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            CurrentScreenOR = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                        } else {
                            mParent.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            CurrentScreenOR = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                        }
                        break;
                    }
                }
                Utils.setWindowSystemUiVisibility(mParent);
            }
        });
        listDialog.show();
    }

    private SharedPreference initPreference(Context context) {
        SharedPreference sharedPreference = new SharedPreference(context);
        COAST_BATTERY = sharedPreference.getCOAST_BATTERY();
        COAST_MINUTE = sharedPreference.getCOAST_MINUTE();
        COAST_FLASH = sharedPreference.getCOAST_FLASH();
        COAST_SWITCH_THEME = sharedPreference.getCOAST_SWITCH_THEME();
        COAST_SWITCH_LIGHT = sharedPreference.getCOAST_SWITCH_LIGHT();
        return sharedPreference;
    }

    private static class SharedPreference {
        private SharedPreferences sharedPreferences;
        private static final String SharedPreferenceFile = "com.wh.mydeskclock.COAST_REC";

        private int COAST_BATTERY = 0;
        private int COAST_MINUTE = 0;
        private int COAST_FLASH = 0;
        private int COAST_SWITCH_THEME = 0;
        private int COAST_SWITCH_LIGHT = 0;

        private int TIME_DAY_HOUR = 6;
        private int TIME_NIGHT_HOUR = 22;

        private static final int DEFAULT_VALUE_INT = 0;

        SharedPreference(Context context) {
            sharedPreferences = context.getSharedPreferences(SharedPreferenceFile, Context.MODE_PRIVATE);
            updateValues();
        }

        void updateValues() {
            COAST_BATTERY = sharedPreferences.getInt("COAST_BATTERY", DEFAULT_VALUE_INT);
            COAST_MINUTE = sharedPreferences.getInt("COAST_MINUTE", DEFAULT_VALUE_INT);
            COAST_FLASH = sharedPreferences.getInt("COAST_FLASH", DEFAULT_VALUE_INT);
            COAST_SWITCH_THEME = sharedPreferences.getInt("COAST_SWITCH_THEME", DEFAULT_VALUE_INT);
            COAST_SWITCH_LIGHT = sharedPreferences.getInt("COAST_SWITCH_LIGHT", DEFAULT_VALUE_INT);
//            TIME_DAY_HOUR = sharedPreferences.getInt("TIME_DAY_HOUR", DEFAULT_VALUE_INT);
//            TIME_NIGHT_HOUR = sharedPreferences.getInt("TIME_NIGHT_HOUR", DEFAULT_VALUE_INT);
        }

        void saveIntSharedPreference(String NAME, int VALUE) {
            SharedPreferences.Editor sharedPreferenceEditor = sharedPreferences.edit();
            sharedPreferenceEditor.putInt(NAME, VALUE);
            sharedPreferenceEditor.apply();
        }

        int getCOAST_BATTERY() {
            return COAST_BATTERY;
        }

        int getCOAST_MINUTE() {
            return COAST_MINUTE;
        }

        int getCOAST_FLASH() {
            return COAST_FLASH;
        }

        int getCOAST_SWITCH_THEME() {
            return COAST_SWITCH_THEME;
        }

        int getCOAST_SWITCH_LIGHT() {
            return COAST_SWITCH_LIGHT;
        }

        public int getTIME_DAY_HOUR() {
            return TIME_DAY_HOUR;
        }

        public int getTIME_NIGHT_HOUR() {
            return TIME_NIGHT_HOUR;
        }

        void setCOAST_BATTERY(int COAST_BATTERY) {
            this.COAST_BATTERY = COAST_BATTERY;
            saveIntSharedPreference("COAST_BATTERY", COAST_BATTERY);
        }

        void setCOAST_MINUTE(int COAST_MINUTE) {
            this.COAST_MINUTE = COAST_MINUTE;
            saveIntSharedPreference("COAST_MINUTE", COAST_MINUTE);
        }

        void setCOAST_FLASH(int COAST_FLASH) {
            this.COAST_FLASH = COAST_FLASH;
            saveIntSharedPreference("COAST_FLASH", COAST_FLASH);
        }

        void setCOAST_SWITCH_THEME(int COAST_SWITCH_THEME) {
            this.COAST_SWITCH_THEME = COAST_SWITCH_THEME;
            saveIntSharedPreference("COAST_SWITCH_THEME", COAST_SWITCH_THEME);

        }

        void setCOAST_SWITCH_LIGHT(int COAST_SWITCH_LIGHT) {
            this.COAST_SWITCH_LIGHT = COAST_SWITCH_LIGHT;
            saveIntSharedPreference("COAST_SWITCH_LIGHT", COAST_SWITCH_LIGHT);
        }

        public void setTIME_DAY_HOUR(int TIME_DAY_HOUR) {
            this.TIME_DAY_HOUR = TIME_DAY_HOUR;
            saveIntSharedPreference("TIME_DAY_HOUR", COAST_SWITCH_LIGHT);
        }

        public void setTIME_NIGHT_HOUR(int TIME_NIGHT_HOUR) {
            this.TIME_NIGHT_HOUR = TIME_NIGHT_HOUR;
            saveIntSharedPreference("TIME_DAY_HOUR", TIME_DAY_HOUR);
        }
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
