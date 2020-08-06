package com.wh.mydeskclock;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.BatteryManager;

import androidx.preference.PreferenceManager;

import com.wh.mydeskclock.app.notify.NotifyRepository;
import com.wh.mydeskclock.app.sticky.StickyRepository;
import com.wh.mydeskclock.app.task.TaskRepository;
import com.wh.mydeskclock.utils.SystemServiceUtils;
import com.wh.mydeskclock.utils.SharedPreferenceUtils;

public class BaseApp extends Application {
    public static SharedPreferences sp_default;
    public static SharedPreferences sp_COAST;

    public static TaskRepository taskRepository;
    public static NotifyRepository notifyRepository;
    public static StickyRepository stickyRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        SystemServiceUtils.batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);

        sp_default = PreferenceManager.getDefaultSharedPreferences(this);
        sp_COAST = getSharedPreferences(SharedPreferenceUtils.sp_coast.FILENAME,MODE_PRIVATE);

        taskRepository = new TaskRepository(this);
        notifyRepository = new NotifyRepository(this);
        stickyRepository = new StickyRepository(this);

    }
}
