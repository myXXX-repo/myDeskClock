package com.wh.mydeskclock.server;

import android.util.Log;

import com.wh.mydeskclock.MainService;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;

import java.util.concurrent.TimeUnit;

public class MainServer {
    private Server server;
    String TAG = "WH_" + MainServer.class.getSimpleName();

    public MainServer(MainService mParent, int port) {
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
}
