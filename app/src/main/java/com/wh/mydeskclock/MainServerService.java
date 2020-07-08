package com.wh.mydeskclock;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.wh.mydeskclock.Utils.NetUtils;
import com.wh.mydeskclock.httpServer.MainServer;


public class MainServerService extends Service {
    String TAG = "WH_" + MainServerService.class.getSimpleName();
    int port = AppConfig.port;
    String URL = "http:/" + NetUtils.getLocalIPAddress() + ":" + port;

    private static NotificationManager notificationManager;
    private MainServer mainServer;

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
        Log.d(TAG, "onCreate: "+URL);
        mainServer = new MainServer(this, port);

        MainServerService.notificationManager = getNotificationManager();

        // 创建通知
        NotificationCompat.Builder frontActivityNotificationBuilder = genForegroundNotification();
        // 启动前台通知
        startForeground(1, frontActivityNotificationBuilder.build());
        // 启动web服务器
        startServer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
    }

    NotificationCompat.Builder genForegroundNotification() {

        return new NotificationCompat.Builder(this, "serverService_Foreground")
                .setContentTitle("Running on " + URL)
                .setSmallIcon(R.drawable.ic_launcher_foreground);
    }

    NotificationManager getNotificationManager() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // 创建通知渠道
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel channel_server_service_foreground = new NotificationChannel(
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
