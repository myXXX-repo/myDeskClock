package com.wh.mydeskclock.app.task;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.utils.SharedPreferenceUtils;
import com.wh.mydeskclock.utils.TimeUtils;

public class DailyTaskWorker extends Worker {
    private static int time = 0;
    private String TAG = "WH_" + getClass().getSimpleName();

    public DailyTaskWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {

        Log.d(TAG, "doWork: " + time);
        time += 1;

        String last_today_date = BaseApp.sp_COAST.getString(SharedPreferenceUtils.sp_coast.DAILY_TASK_LAST_CREATE_DATE, "0") + "";
        String today_date = TimeUtils.getTodayDateYMD();
        if (last_today_date.equals(today_date)) {
            Log.d(TAG, "doWork: daily tasks created today");
            return Result.success();
        }
        createDailyTask();
        BaseApp.sp_COAST.edit().putString(SharedPreferenceUtils.sp_coast.DAILY_TASK_LAST_CREATE_DATE, today_date).apply();

        return Result.success();
    }

    private void createDailyTask() {
        String task_from_preference = BaseApp.sp_default.getString(SharedPreferenceUtils.sp_default.SETTING_TASK_DAILY_TASK_LIST,"")+"";
        if(task_from_preference.equals("")){
            Log.d(TAG, "createDailyTask: get null of preference tasks");
            return;
        }
        String device = "localhost";
        String title = "dailyTask";
        String day_date = TimeUtils.getTodayDateYMD();
        String[] tasks = new String[]{
                "早上喝两袋硒","晚上喝两袋硒",
                "填垃圾表","背英语单词","刮胡子"
        };
        tasks = task_from_preference.split("\n");
        Task[] taskList = new Task[tasks.length];
        for (int i = 0; i < tasks.length; i++) {
            taskList[i] = new Task(day_date + " " + tasks[i], title, device);
        }
        BaseApp.taskRepository.insert(taskList);
        Log.d(TAG, "createDailyTask: "+TimeUtils.getFormattedTime(System.currentTimeMillis()));
        Log.d(TAG, "createDailyTask: daily tasks create done");
    }
}
