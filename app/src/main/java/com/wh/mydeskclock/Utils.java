package com.wh.mydeskclock;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Utils {
    static final int TEST = 1;
    static final int NONE_TEST = 0;

    // 数字转汉字 for 星期
    static String num2Chinese(int num) {
        String[] c = {"六", "日", "一", "二", "三", "四", "五"};
        if (num > -1 && num < c.length) {
            return c[num];
        } else {
            return c[0];
        }
    }

    // 补全两位数字 for 分钟
    static String ensure2Numbers(int num) {
        if (num >= 0 && num < 10) {
            return "0" + num;
        } else {
            return String.valueOf(num);
        }
    }

    // 黑色白色互相转换
    static int b2w2b(int color) {
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

    // 设置窗口屏幕亮度
    static void setWindowBrightness(Activity mParent, int brightness) {
        Window window = mParent.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

    // 设置窗口系统显示 主要用于全屏--隐藏标题栏 状态栏 动作栏
    public static void setWindowSystemUiVisibility(Activity mParent) {
        mParent.getWindow().getDecorView().setSystemUiVisibility(getHideSystemUIFlags());
    }

    public static int getHideSystemUIFlags() {
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
    }

    static void toggleDarkMode_tv(
            TextView tv_hour, TextView tv_min, TextView tv_week, TextView tv_battery, int TextColor) {
        tv_hour.setTextColor(TextColor);
        tv_min.setTextColor(TextColor);
        tv_week.setTextColor(TextColor);
        tv_battery.setTextColor(TextColor);
    }

    static boolean Check(Context context, int STATUS) {
        // change here to get test
        if (STATUS == Utils.TEST) {
            return false;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        try {
            Date date = simpleDateFormat.parse(context.getString(R.string.time_limit));
            long aimedTimeMS;
            if (date != null) {
                aimedTimeMS = date.getTime();
            } else {
                return false;
            }
            long currentDateMS = System.currentTimeMillis();
            return currentDateMS < aimedTimeMS;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getCurrentTimeDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static void pausePlay() {
        sendKeyCode(85);
    }

    public static void nextPlay() {
        sendKeyCode(87);
    }

    public static void previousPlay() {
        sendKeyCode(88);
    }

    // 发送键码
    private static void sendKeyCode(final int KeyCode) {
        new Thread() {
            @Override
            public void run() {
                Instrumentation instrumentation = new Instrumentation();
                instrumentation.sendKeyDownUpSync(KeyCode);
            }
        }.start();
    }

    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
//            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }



}
