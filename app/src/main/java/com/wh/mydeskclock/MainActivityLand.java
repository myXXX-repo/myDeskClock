package com.wh.mydeskclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;

import com.wh.mydeskclock.utils.UiUtils;

public class MainActivityLand extends BaseActivity {
    private boolean SETTING_UI_RE_LAND;
    private View v_cover;
    private static MyHandler myHandler;
    private MainFragmentLand mainFragmentLand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_land);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SETTING_UI_RE_LAND = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_RE_LAND, false);

        if (SETTING_UI_RE_LAND) {
            UiUtils.setScreenOR_LAND_RE(this);
        }

        mainFragmentLand = new MainFragmentLand();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_main_land, mainFragmentLand).commit();

        v_cover = findViewById(R.id.v_cover);

        myHandler = new MyHandler();

        flash();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void flash(){
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(100);
                    myHandler.sendEmptyMessage(MyHandler.WHAT_SET_BLACK);
                    myHandler.sendEmptyMessage(MyHandler.WHAT_SET_VISIBLE);
                    sleep(700);
                    myHandler.sendEmptyMessage(MyHandler.WHAT_SET_WHITE);
                    sleep(500);
                    myHandler.sendEmptyMessage(MyHandler.WHAT_SET_GONE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    class MyHandler extends Handler {
        private static final int WHAT_SET_BLACK = 721;
        private static final int WHAT_SET_WHITE = 903;
        private static final int WHAT_SET_GONE = 203;
        private static final int WHAT_SET_VISIBLE = 751;

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WHAT_SET_BLACK: {
                    v_cover.setBackgroundColor(Color.BLACK);
                    break;
                }
                case WHAT_SET_WHITE: {
                    v_cover.setBackgroundColor(Color.WHITE);
                    break;
                }
                case WHAT_SET_GONE: {
                    v_cover.setVisibility(View.GONE);
                    break;
                }
                case WHAT_SET_VISIBLE: {
                    v_cover.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }
    }
}