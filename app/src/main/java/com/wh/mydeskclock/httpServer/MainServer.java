package com.wh.mydeskclock.httpServer;

import android.util.Log;

import com.wh.mydeskclock.MainServerService;
import com.yanzhenjie.andserver.AndServer;
import com.yanzhenjie.andserver.Server;

import java.util.concurrent.TimeUnit;

public class MainServer {
    private Server server;
    private MainServerService mParent;
    String TAG = "WH_" + MainServer.class.getSimpleName();

    public MainServer(MainServerService mParent, int port) {
        this.mParent = mParent;
//        server = AndServer.serverBuilder(mParent)
        server = AndServer.serverBuilder()
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
