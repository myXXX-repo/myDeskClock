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

//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        boolean SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER, true);
//        int SETTING_HTTP_SERVER_PORT = Integer.parseInt(sharedPreferences.getString(Config.DefaultSharedPreferenceKey.SETTING_HTTP_SERVER_PORT,"8081"));
//
//        if (SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER) {
//            startService(new Intent(this, MainService.class));
//        }

    }
}
