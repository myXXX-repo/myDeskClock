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

import androidx.core.app.NotificationCompat;

import com.wh.mydeskclock.utils.NetUtils;
import com.wh.mydeskclock.server.MainServer;
import com.wh.mydeskclock.utils.SharedPreferenceUtils;

import java.util.Objects;


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

        SETTING_HTTP_SERVER_PORT = Integer.parseInt(Objects.requireNonNull(BaseApp.sp_default.getString(SharedPreferenceUtils.sp_default.SETTING_HTTP_SERVER_PORT, "8081")));
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

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopServer();
        if(broadcastReceiver!=null){
            unregisterReceiver(broadcastReceiver);
        }
    }

    NotificationCompat.Builder genForegroundNotification() {
        Intent intent_exit = new Intent();
        intent_exit.setAction("myDeskClock_server_exit");
        PendingIntent pendingIntent_exit = PendingIntent.getBroadcast(MainService.this,0,intent_exit,0);

        return new NotificationCompat.Builder(this, "serverService_Foreground")
                .setContentTitle("Running on " + URL)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .addAction(R.drawable.ic_launcher_foreground,"exit",pendingIntent_exit);
    }

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
