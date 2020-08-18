package com.wh.mydeskclock.server;

import android.util.Log;

import com.wh.mydeskclock.BaseApp;
import com.wh.mydeskclock.MainService;
import com.wh.mydeskclock.utils.ApiNode;
import com.wh.mydeskclock.utils.SharedPreferenceUtils;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainServer {
    private Server server;
    String TAG = "WH_" + MainServer.class.getSimpleName();

    public static List<ApiNode> apiList;
    public static String access_token = BaseApp.sp_default.getString(SharedPreferenceUtils.sp_default.SETTING_HTTP_SERVER_ACCESS_TOKEN,"0");


    public MainServer(MainService mParent, int port) {
        apiList = new ArrayList<>();
        server = AndServer.webServer(mParent)
                .port(port)
                .timeout(10, TimeUnit.SECONDS)
                .listener(new Server.ServerListener() {
                    @Override
                    public void onStarted() {
                        Log.d(TAG, "onStarted");
                    }

                    @Override
                    public void onStopped() {
                        Log.d(TAG, "onStopped");
                    }

                    @Override
                    public void onException(Exception e) {
                        Log.d(TAG, "onException");
                    }
                })
                .build();
    }

    public void startServer() {
        if (!server.isRunning()) {
            server.startup();
        }
    }

    public void stopServer() {
        if (server.isRunning()) {
            server.shutdown();
        }
    }

    public static boolean authNotGot(String ACCESS_TOKEN){
        return !access_token.equals("0") && !access_token.equals(ACCESS_TOKEN);
    }
}
