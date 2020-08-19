package com.wh.mydeskclock.utils;


import android.app.DownloadManager;
import android.content.Context;
import android.media.AudioManager;
import android.os.BatteryManager;

public class SystemServiceUtils {
    public static BatteryManager batteryManager;

    public static int getBatteryLevel(Context context) {
        if (SystemServiceUtils.batteryManager == null) {
            SystemServiceUtils.batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
        }
        return SystemServiceUtils.batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }

    public static int getBatteryLevel() {
        if (SystemServiceUtils.batteryManager != null) {
            return SystemServiceUtils.batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        }
        return -1;
    }

    public static DownloadManager getDownloadManager(Context context){
        return (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    }
}
