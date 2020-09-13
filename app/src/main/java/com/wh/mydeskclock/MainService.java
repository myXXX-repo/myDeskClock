package com.wh.mydeskclock;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.wh.mydeskclock.app.task.DailyTaskWorker;
import com.wh.mydeskclock.utils.NetUtils;
import com.wh.mydeskclock.server.MainServer;
import com.wh.mydeskclock.utils.SharedPreferenceUtils;

import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class MainService extends Service {
    String TAG = "WH_" + MainService.class.getSimpleName();
    String URL;

    private static NotificationManager notificationManager;
    private MainServer mainServer;
    private int SETTING_HTTP_SERVER_PORT;
    private BroadcastReceiver broadcastReceiver;

    void startServer() {
        mainServer.startServer();
    }

    void stopServer() {
        mainServer.stopServer();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 服务器端口
        SETTING_HTTP_SERVER_PORT = Integer.parseInt(Objects.requireNonNull(BaseApp.sp_default.getString(SharedPreferenceUtils.sp_default.SETTING_HTTP_SERVER_PORT, "8081")));

        // 服务器链接
        URL = "http:/" + NetUtils.getLocalIPAddress() + ":" + SETTING_HTTP_SERVER_PORT;

        mainServer = new MainServer(this, SETTING_HTTP_SERVER_PORT);

        notificationManager = getNotificationManager();

        // 创建通知
        NotificationCompat.Builder frontActivityNotificationBuilder = genForegroundNotification();
        // 启动前台通知
        startForeground(1, frontActivityNotificationBuilder.build());
        // 启动web服务器
        startServer();


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MainService.this.stopSelf();
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("myDeskClock_server_exit");
        registerReceiver(broadcastReceiver,intentFilter);


        String tmp_task = BaseApp.sp_default.getString(SharedPreferenceUtils.sp_default.SETTING_TASK_DAILY_TASK_LIST, "") + "";
        if (tmp_task.equals("")) {
            Log.d(TAG, "onCreate: get null of preference task list");
        } else {
            Log.d(TAG, "onCreate: creating new work queue");
            PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest
                    .Builder(DailyTaskWorker.class, 3, TimeUnit.HOURS)
                    .addTag("daily_task")
                    .build();
            WorkManager.getInstance(this).enqueueUniquePeriodicWork("daily_task_", ExistingPeriodicWorkPolicy.REPLACE, periodicWorkRequest);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
        // 解除广播接收器的注册
        if(broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    // 生成前台通知
    NotificationCompat.Builder genForegroundNotification() {
        Intent intent_exit = new Intent();
        intent_exit.setAction("myDeskClock_server_exit");
        PendingIntent pendingIntent_exit = PendingIntent.getBroadcast(MainService.this,0,intent_exit,0);

        return new NotificationCompat.Builder(this, "serverService_Foreground")
                .setContentTitle("Running on " + URL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .addAction(R.drawable.ic_launcher_foreground,"exit",pendingIntent_exit);
    }

    // 获取通知管理器
    NotificationManager getNotificationManager() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建通知渠道
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel_server_service_foreground = new NotificationChannel(
                    "serverService_Foreground",
                    "服务器前台活动",
                    NotificationManager.IMPORTANCE_MIN);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel_server_service_foreground);
            }
        }
        return notificationManager;
    }
}
