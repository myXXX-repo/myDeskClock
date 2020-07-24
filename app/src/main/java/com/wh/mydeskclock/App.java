package com.wh.mydeskclock;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;

import androidx.preference.PreferenceManager;

import com.wh.mydeskclock.utils.HardwareUtils;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        HardwareUtils.batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);
    }
}
