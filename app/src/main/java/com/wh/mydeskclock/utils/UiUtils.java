package com.wh.mydeskclock.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class UiUtils {


    /**
     * @describe 获取界面全屏的标签
     * @args Void null
     * @return int 界面标签
     * */
    public static int getHideSystemUIFlags() {
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
    }

    /**
     * @describe 设置全屏
     * @args Window 输入界面的窗口
     * @return Void null
     * */
    public static void setFullScreen(final Window window) {
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    /**
     * @describe 设置屏幕方向为反向横屏
     * @args AppCompactActivity
     * @return Void null
     * */
    public static void setScreenOR_LAND_RE(AppCompatActivity mParent) {
        setScreenOR(mParent, ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    /**
     * @describe 设置屏幕方向为横屏
     * @args AppCompactActivity
     * @return Void null
     * */
    public static void setScreenOR_LAND(AppCompatActivity mParent) {
        setScreenOR(mParent, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * @describe 设置屏幕方向为反向竖屏
     * @args AppCompactActivity
     * @return Void null
     * */
    public static void setScreenOR_PORT_RE(AppCompatActivity mParent) {
        setScreenOR(mParent, ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
    }

    /**
     * @describe 设置屏幕方向为竖屏
     * @args AppCompactActivity
     * @return Void null
     * */
    public static void setScreenOR_PORT(AppCompatActivity mParent) {
        setScreenOR(mParent, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * @describe 设置屏幕方向
     * @args AppCompactActivity
     *       int 屏幕方向
     * @return Void null
     * */
    public static void setScreenOR(AppCompatActivity mParent, int OR) {
        mParent.setRequestedOrientation(OR);
    }

    /**
     * @describe 设置屏幕背光亮度
     * @args Activity, int 亮度数值
     * @return Void null
     * */
    public static void setWindowBrightness(Activity mParent, int brightness) {
        Window window = mParent.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

    /**
     * @describe 设置屏幕背光开启
     * @args Activity
     * @return Void null
     * */
    public static void setLightOn(Activity mParent) {
        setWindowBrightness(mParent, 1);
        Utils.pf_coast_int_add(SharedPreferenceUtils.sp_coast.COAST_LIGHT_ON);
    }

    /**
     * @describe 设置屏幕背光关闭
     * @args Activity
     * @return Void null
     * */
    public static void setLightOff(Activity mParent) {
        setWindowBrightness(mParent, 0);
        Utils.pf_coast_int_add(SharedPreferenceUtils.sp_coast.COAST_LIGHT_OFF);
    }


    /**
     * @describe 设置时间日期
     * @args ...
     * @return Void null
     * */
    public static void setTime_MainFragment(TextView tv_hour, TextView tv_min, TextView tv_week, TextView tv_date) {
        Calendar calendar = Calendar.getInstance();

        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        tv_week.setText(String.format("星期%s", TimeUtils.num2Chinese(weekday)));
        tv_hour.setText(Utils.ensure2Numbers(hour));
        tv_min.setText(Utils.ensure2Numbers(min));
        tv_date.setText(TimeUtils.getFormattedTime(System.currentTimeMillis(), TimeUtils.yMdTimeFormat));
    }

    /**
     * @describe 设置时间日期
     * @args ...
     * @return Void null
     * */
    public static void setBattery_MainFragment(TextView tv_battery){
        int level = SystemServiceUtils.getBatteryLevel();
        String level_ = level+"%";
        tv_battery.setText(level_);
    }

}
