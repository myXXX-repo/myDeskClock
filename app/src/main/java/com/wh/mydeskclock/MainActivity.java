package com.wh.mydeskclock;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.wh.mydeskclock.utils.UiUtils;

public class MainActivity extends AppCompatActivity {
    String TAG = "WH_" + MainActivity.class.getSimpleName();
    private SharedPreferences sharedPreferences;
    private boolean SETTING_UI_RE_LAND;
    private boolean SETTING_UI_LAND;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SETTING_UI_RE_LAND = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_RE_LAND,false);
        SETTING_UI_LAND = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_LAND,true);

        if(SETTING_UI_LAND){
            if(SETTING_UI_RE_LAND){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onCreate(savedInstanceState);
        UiUtils.setFullScreen(getWindow());
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        startService(new Intent(MainActivity.this, MainService.class));
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        UiUtils.setFullScreen(getWindow());
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiUtils.setFullScreen(getWindow());

        SETTING_UI_RE_LAND = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_RE_LAND,false);
        SETTING_UI_LAND = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_LAND,true);
        if(SETTING_UI_LAND){
            if(SETTING_UI_RE_LAND){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
            }else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        UiUtils.setFullScreen(getWindow());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static class MyHandler extends Handler {
        private static final int WHAT_SET_VISIBLE = 986;
        private static final int WHAT_SET_GONE = 512;
        private static final int WHAT_SET_BLACK = 513;
        private static final int WHAT_SET_WHITE = 514;

        private View v_cover;

        public MyHandler(AppCompatActivity mParent) {
            this.v_cover = mParent.findViewById(R.id.v_cover);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WHAT_SET_VISIBLE: {
                    v_cover.setVisibility(View.VISIBLE);
                    break;
                }
                case WHAT_SET_GONE: {
                    v_cover.setVisibility(View.GONE);
                    break;
                }
                case WHAT_SET_BLACK: {
                    v_cover.setBackgroundColor(Color.BLACK);
                    break;
                }
                case WHAT_SET_WHITE: {
                    v_cover.setBackgroundColor(Color.WHITE);
                    break;
                }
            }
        }
    }

    public static void flashScreen(final MyHandler myHandler){
        new Thread() {
            @Override
            public void run() {
                try {
                    myHandler.sendEmptyMessage(MyHandler.WHAT_SET_VISIBLE);
                    myHandler.sendEmptyMessage(MyHandler.WHAT_SET_WHITE);
                    sleep(300);
                    myHandler.sendEmptyMessage(MyHandler.WHAT_SET_BLACK);
                    sleep(300);
                    myHandler.sendEmptyMessage(MyHandler.WHAT_SET_WHITE);
                    sleep(900);
                    myHandler.sendEmptyMessage(MyHandler.WHAT_SET_GONE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}