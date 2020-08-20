package com.wh.mydeskclock;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.BatteryManager;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceManager;

import com.wh.mydeskclock.app.notify.NotifyRepository;
import com.wh.mydeskclock.app.sticky.StickyRepository;
import com.wh.mydeskclock.app.tab.TabRepository;
import com.wh.mydeskclock.app.task.TaskRepository;
import com.wh.mydeskclock.utils.AppUtils;
import com.wh.mydeskclock.utils.SystemServiceUtils;
import com.wh.mydeskclock.utils.SharedPreferenceUtils;
import com.wh.mydeskclock.widget.MyDialog;

import java.io.File;
import java.io.IOException;

public class BaseApp extends Application {
    private String TAG = "WH_"+getClass().getSimpleName();
    public static SharedPreferences sp_default;
    public static SharedPreferences sp_COAST;

    public static TaskRepository taskRepository;
    public static NotifyRepository notifyRepository;
    public static StickyRepository stickyRepository;
    public static TabRepository tabRepository;

    public static boolean isDebug = false;

    @Override
    public void onCreate() {
        super.onCreate();
        SystemServiceUtils.batteryManager = (BatteryManager) getSystemService(Context.BATTERY_SERVICE);

        sp_default = PreferenceManager.getDefaultSharedPreferences(this);
        sp_COAST = getSharedPreferences(SharedPreferenceUtils.sp_coast.FILENAME,MODE_PRIVATE);

        taskRepository = new TaskRepository(this);
        notifyRepository = new NotifyRepository(this);
        stickyRepository = new StickyRepository(this);
        tabRepository = new TabRepository(this);


        isDebug = isIsDebug();

        Log.d(TAG, "onCreate: isDebug"+isDebug);

    }

    private boolean isIsDebug(){
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()+"/test_myDC");
        Resources resources = getResources();
        String type = resources.getString(R.string.type);
        return (file.exists()&&file.isFile())||"debug".equals(type);
    }
}
