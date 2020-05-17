package com.wh.mydeskclock;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;

import androidx.lifecycle.ViewModel;

import com.mySync.SharedPreferenceUtils;

public class MainClockViewModel extends ViewModel {
    private SharedPreferenceUtils sharedPreferenceUtils = null;
    private int lastBatteryLevel = -1;

    private int STATUS_SCREEN_OR = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    private int STATUS_SCREEN_BRIGHTNESS = 0;
    private int STATUS_BACKGROUND_COLOR = Color.WHITE;
    private int COAST_BATTERY = 0;
    private int COAST_MINUTE = 0;
    public int getCoastMinute(){
        COAST_MINUTE = sharedPreferenceUtils.getCOAST_MINUTE();
        return COAST_MINUTE;
    }
    public void saveAddCoastMinute(){
        COAST_MINUTE+=1;
        sharedPreferenceUtils.setCOAST_MINUTE(COAST_MINUTE);
    }
    private int COAST_FLASH = 0;
    private int COAST_SWITCH_THEME = 0;
    private int COAST_SWITCH_LIGHT = 0;

    public MainClockViewModel(Context context) {
        sharedPreferenceUtils = new SharedPreferenceUtils(context);
    }
}
