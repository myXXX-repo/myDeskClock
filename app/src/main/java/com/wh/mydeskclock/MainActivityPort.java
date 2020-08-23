package com.wh.mydeskclock;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.wh.mydeskclock.utils.AppUtils;
import com.wh.mydeskclock.utils.SharedPreferenceUtils;
import com.wh.mydeskclock.utils.UiUtils;
import com.wh.mydeskclock.utils.Utils;
import com.wh.mydeskclock.widget.MyDialog;

import java.io.IOException;

public class MainActivityPort extends BaseActivity implements View.OnClickListener {
    private View v_cover;
    private MyHandler myHandler;
    private BroadcastReceiver broadcastReceiver;
    private TextView tv_date;
    private TextView tv_battery;

    private static boolean SETTING_UI_AUTO_FLASH_SCREEN;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_port);

        init();

        SETTING_UI_AUTO_FLASH_SCREEN = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_UI_AUTO_FLASH_SCREEN, false);


        flash();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    boolean hasNewVersion = AppUtils.checkUpdate(TAG, MainActivityPort.this);
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
    protected void onResume() {
        super.onResume();
        SETTING_UI_AUTO_FLASH_SCREEN = BaseApp.sp_default.getBoolean(SharedPreferenceUtils.sp_default.SETTING_UI_AUTO_FLASH_SCREEN, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }

    public void flash() {
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hour: {
                flash();
                break;
            }
            case R.id.tv_min: {
                startActivity(new Intent(MainActivityPort.this, SettingActivity.class));
                break;
            }
            case R.id.tv_battery: {
                AlertDialog.Builder battery_coast_detail = new AlertDialog.Builder(MainActivityPort.this)
                        .setTitle("Battery Coast Detail")
                        .setMessage("null")
                        .setPositiveButton("Close", null);
                MyDialog myDialog = new MyDialog(battery_coast_detail);
                myDialog.setFullScreen();
                myDialog.show(getSupportFragmentManager(), "battery_coast_detail");
                break;
            }
            case R.id.tv_week: {
                boolean showOthers = tv_date.getVisibility() == View.VISIBLE;
                ImageView iv_battery_ico = findViewById(R.id.iv_battery_ico);
                if (showOthers) {
                    tv_date.setVisibility(View.GONE);
                    tv_battery.setVisibility(View.GONE);
                    iv_battery_ico.setVisibility(View.GONE);
                } else {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_battery.setVisibility(View.VISIBLE);
                    iv_battery_ico.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }

    private void init() {
        TextView tv_hour = findViewById(R.id.tv_hour);
        TextView tv_min = findViewById(R.id.tv_min);
        TextView tv_week = findViewById(R.id.tv_week);
        tv_date = findViewById(R.id.tv_date);
        tv_battery = findViewById(R.id.tv_battery);

        tv_hour.setOnClickListener(this);
        tv_min.setOnClickListener(this);
        tv_week.setOnClickListener(this);
        tv_battery.setOnClickListener(this);

        v_cover = findViewById(R.id.v_cover);

        myHandler = new MyHandler(
                tv_hour,
                tv_min,
                tv_week,
                tv_date,
                tv_battery,
                v_cover);

        broadcastReceiver = new BatteryTime_BroadcastReceiver(myHandler);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(broadcastReceiver, intentFilter);

        UiUtils.setTime_MainFragment(tv_hour, tv_min, tv_week, tv_date);
        UiUtils.setBattery_MainFragment(tv_battery);
    }

    class MyHandler extends Handler {
        private static final int WHAT_SET_BLACK = 721;
        private static final int WHAT_SET_WHITE = 903;
        private static final int WHAT_SET_GONE = 203;
        private static final int WHAT_SET_VISIBLE = 751;

        private static final int WHAT_TIME = 338;
        private static final int WHAT_BATTERY = 33;
        private static final int WHAT_UPDATE = 336;

        private TextView tv_hour;
        private TextView tv_min;
        private TextView tv_week;
        private TextView tv_date;
        private TextView tv_battery;
        private View v_cover;

        public MyHandler(TextView tv_hour, TextView tv_min, TextView tv_week, TextView tv_date, TextView tv_battery,View v_cover) {
            this.tv_hour = tv_hour;
            this.tv_min = tv_min;
            this.tv_week = tv_week;
            this.tv_date = tv_date;
            this.tv_battery = tv_battery;
            this.v_cover = v_cover;
        }

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
                case WHAT_TIME: {
                    UiUtils.setTime_MainFragment(tv_hour, tv_min, tv_week, tv_date);
                    break;
                }
                case WHAT_BATTERY: {
                    UiUtils.setBattery_MainFragment(tv_battery);
                    break;
                }
                case WHAT_UPDATE: {
                    MyDialog myDialog = new MyDialog(
                            new AlertDialog.Builder(MainActivityPort.this)
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

    private class BatteryTime_BroadcastReceiver extends BroadcastReceiver {
        MyHandler myHandler;
        private int FlashDistanceTime = 100;

        public BatteryTime_BroadcastReceiver(MyHandler myHandler) {
            this.myHandler = myHandler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String ACTION = intent.getAction();
            assert ACTION != null;
            switch (ACTION) {
                case Intent.ACTION_TIME_TICK: {
                    myHandler.sendEmptyMessage(MyHandler.WHAT_TIME);
                    if (SETTING_UI_AUTO_FLASH_SCREEN) {
                        FlashDistanceTime--;
                        if (FlashDistanceTime == 0) {
                            FlashDistanceTime = 100;
                            flash();
                        }
                    }
                    break;
                }
                case Intent.ACTION_BATTERY_CHANGED: {
                    myHandler.sendEmptyMessage(MyHandler.WHAT_BATTERY);
                    break;
                }
            }
        }
    }
}