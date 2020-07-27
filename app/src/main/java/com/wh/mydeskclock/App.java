package com.wh.mydeskclock;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.BatteryManager;

import androidx.preference.PreferenceManager;

import com.wh.mydeskclock.app.notify.NotifyRepository;
import com.wh.mydeskclock.app.task.TaskRepository;
import com.wh.mydeskclock.utils.ApiNode;
import com.wh.mydeskclock.utils.HardwareUtils;

import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    public static SharedPreferences sp_default;
    public static SharedPreferences sp_COAST;

    public static TaskRepository taskRepository;
    public static NotifyRepository notifyRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        HardwareUtils.batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);

        sp_default = PreferenceManager.getDefaultSharedPreferences(this);
        sp_COAST = getSharedPreferences(SharedPreferenceUtils.sp_coast.FILENAME,MODE_PRIVATE);

        taskRepository = new TaskRepository(this);
        notifyRepository = new NotifyRepository(this);

    }
}
