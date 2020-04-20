package com.wh.mydeskclock;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class Utils {

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
        mParent.getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    static void toggleDarkMode_tv(
            TextView tv_hour, TextView tv_min, TextView tv_week, TextView tv_battery ,int TextColor) {
        tv_hour.setTextColor(TextColor);
        tv_min.setTextColor(TextColor);
        tv_week.setTextColor(TextColor);
        tv_battery.setTextColor(TextColor);
    }

}
