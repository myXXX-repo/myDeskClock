package com.wh.mydeskclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;

import com.wh.mydeskclock.utils.AppUtils;
import com.wh.mydeskclock.utils.SharedPreferenceUtils;
import com.wh.mydeskclock.utils.UiUtils;
import com.wh.mydeskclock.utils.Utils;
import com.wh.mydeskclock.widget.MyDialog;

import java.io.IOException;

public class MainActivityLand extends BaseActivity {
    private boolean SETTING_UI_RE_LAND;
    private View v_cover;
    private static MyHandler myHandler;
    private MainFragmentLand mainFragmentLand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_land);

        SETTING_UI_RE_LAND = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_UI_RE_LAND, false);

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

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    boolean hasNewVersion = AppUtils.checkUpdate(TAG, MainActivityLand.this);
                    if (hasNewVersion) {
                        myHandler.sendEmptyMessage(MyHandler.WHAT_UPDATE);
                    }
                } catch (PackageManager.NameNotFoundException | IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void flash() {

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

                    Utils.pf_coast_int_add(SharedPreferenceUtils.sp_coast.COAST_FLASH_SCREEN);
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
        private static final int WHAT_UPDATE = 7511;

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
                case WHAT_UPDATE: {
                    MyDialog myDialog = new MyDialog(
                            new AlertDialog.Builder(MainActivityLand.this)
                                    .setTitle("Find New Version")
                                    .setMessage("Get Update in CoolApk ?")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coolapk.com/apk/260552"));
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("cancel", null)
                    );
                    myDialog.setFullScreen();
                    myDialog.show(getSupportFragmentManager(), "check update");
                    break;
                }
            }
        }
    }
}