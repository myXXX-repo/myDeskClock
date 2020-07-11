package com.wh.mydeskclock;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
//    private View v_cover;
    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiUtils.setFullScreen(getWindow());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.d(TAG, "onCreate: ");
        startService(new Intent(MainActivity.this, MainService.class));
        myHandler = new MyHandler(this);

//        flashScreen(myHandler);
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