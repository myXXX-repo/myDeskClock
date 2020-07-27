package com.wh.mydeskclock.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class UiUtils {
    public static int getHideSystemUIFlags() {
        return View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
    }
    public static void setFullScreen(final Window window){
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public static void setScreenOR_LAND_RE(AppCompatActivity mParent){
        setScreenOR(mParent, ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
    }

    public static void setScreenOR_LAND(AppCompatActivity mParent){
        setScreenOR(mParent, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public static void setScreenOR_PORT_RE(AppCompatActivity mParent){
        setScreenOR(mParent, ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
    }

    public static void setScreenOR_PORT(AppCompatActivity mParent){
        setScreenOR(mParent, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public static void setScreenOR(AppCompatActivity mParent, int OR){
        mParent.setRequestedOrientation(OR);
    }

    // 设置窗口屏幕亮度
    public static void setWindowBrightness(Activity mParent, int brightness) {
        Window window = mParent.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }

}
