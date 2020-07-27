package com.wh.mydeskclock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.wh.mydeskclock.app.mediaCtrl.MediaCtrlFragment;
import com.wh.mydeskclock.app.notify.NotifyFragment;
import com.wh.mydeskclock.app.settings.SettingActivity;
import com.wh.mydeskclock.app.task.TaskListFragment;
import com.wh.mydeskclock.utils.HardwareUtils;
import com.wh.mydeskclock.utils.NetUtils;
import com.wh.mydeskclock.utils.QRCodeGenerator;
import com.wh.mydeskclock.utils.TimeUtils;
import com.wh.mydeskclock.utils.UiUtils;
import com.wh.mydeskclock.utils.Utils;
import com.wh.mydeskclock.widget.MyDialog;

import java.util.Calendar;
import java.util.Objects;

public class MainFragmentLand extends BaseFragment implements View.OnClickListener {

    private TextView tv_hour;
    private TextView tv_min;
    private TextView tv_address;
    private TextView tv_battery;
    private TextView tv_week;
    private TextView tv_date;
    private BroadcastReceiver broadcastReceiver;
    private FrameLayout fl_notify;
    private FrameLayout fl_media_ctrl;
    private FrameLayout fl_task;
    private MyHandler myHandler;
    private SharedPreferences sharedPreferences;
    private boolean SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER;
    private boolean SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL;
    private int SETTING_HTTP_SERVER_PORT = 0;
    private boolean SETTING_UI_SHOW_SERVER_ADDRESS;
    private boolean SETTING_TASK_HIDE_DONE;
    private FragmentManager fragmentManager;

    private int FlashDistanceTime = 100;
    private boolean SETTING_UI_AUTO_FLASH_SCREEN;

    public MainFragmentLand() {
        super.setTAG("WH_" + getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_land, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        SETTING_HTTP_SERVER_PORT = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString(Config.DefaultSharedPreferenceKey.SETTING_HTTP_SERVER_PORT, "8081")));
        SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER, true);
        SETTING_UI_SHOW_SERVER_ADDRESS = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_SHOW_SERVER_ADDRESS, true);
        SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL, true);
        SETTING_UI_AUTO_FLASH_SCREEN = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_AUTO_FLASH_SCREEN,false);
        if (SETTING_TASK_HIDE_DONE != sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_TASK_HIDE_DONE, true)) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_task,new TaskListFragment()).commit();
        }
        if (SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL) {
            fl_media_ctrl.setVisibility(View.VISIBLE);
        } else {
            fl_media_ctrl.setVisibility(View.GONE);
        }
        if (SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER) {
            requireContext().startService(new Intent(requireContext(), MainService.class));
            if (SETTING_UI_SHOW_SERVER_ADDRESS) {
                tv_address.setText("http:/" + NetUtils.getLocalIPAddress() + ":" + SETTING_HTTP_SERVER_PORT);
                tv_address.setVisibility(View.VISIBLE);
            } else {
                tv_address.setVisibility(View.GONE);
            }
        } else {
            tv_address.setVisibility(View.GONE);
            Intent intent = new Intent();
            intent.setAction("myDeskClock_server_exit");
            requireContext().sendBroadcast(intent);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            requireContext().unregisterReceiver(broadcastReceiver);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hour: {
                MainActivityLand.flash();
                break;
            }
            case R.id.tv_min: {
                startActivity(new Intent(requireActivity(), SettingActivity.class));
                break;
            }
            case R.id.tv_address: {
                ImageView iv_address_qrcode = new ImageView(requireContext());
                QRCodeGenerator qrCodeGenerator = new QRCodeGenerator("http:/" + NetUtils.getLocalIPAddress() + ":" + SETTING_HTTP_SERVER_PORT, 500, 500);
                iv_address_qrcode.setImageBitmap(qrCodeGenerator.getQRCode());
                AlertDialog.Builder address_builder = new AlertDialog.Builder(requireContext())
                        .setView(iv_address_qrcode);
                MyDialog myDialog = new MyDialog(address_builder);
                myDialog.setFullScreen();
                myDialog.show(requireActivity().getSupportFragmentManager(), "address_qrcode_dialog");
                break;
            }
            case R.id.tv_battery: {
                AlertDialog.Builder battery_coast_detail = new AlertDialog.Builder(requireContext())
                        .setTitle("Battery Cost Detail")
                        .setMessage("null")
                        .setPositiveButton("Close", null);
                MyDialog myDialog = new MyDialog(battery_coast_detail);
                myDialog.setFullScreen();
                myDialog.show(requireActivity().getSupportFragmentManager(), "battery_coast_detail");
                break;
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private void init() {

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SETTING_HTTP_SERVER_PORT = Integer.parseInt(Objects.requireNonNull(sharedPreferences.getString(Config.DefaultSharedPreferenceKey.SETTING_HTTP_SERVER_PORT, "8081")));
        SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER, true);
        SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL, true);
        SETTING_UI_SHOW_SERVER_ADDRESS = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_SHOW_SERVER_ADDRESS, true);
        SETTING_TASK_HIDE_DONE = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_TASK_HIDE_DONE, true);
        SETTING_UI_AUTO_FLASH_SCREEN = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_AUTO_FLASH_SCREEN,false);

        if (SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER) {
            requireContext().startService(new Intent(requireContext(), MainService.class));
        }

        tv_hour = requireActivity().findViewById(R.id.tv_hour);
        tv_min = requireActivity().findViewById(R.id.tv_min);
        tv_min.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                MyDialog myDialog = new MyDialog(
                        new AlertDialog.Builder(requireContext())
                                .setTitle("test")
                .setItems(new String[]{"打开背光", "关闭背光"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:{
                                UiUtils.setWindowBrightness(requireActivity(),1);
                                break;
                            }
                            case 1:{
                                UiUtils.setWindowBrightness(requireActivity(),0);
                                break;
                            }
                        }
                    }
                }));
                myDialog.setFullScreen();
                myDialog.show(requireActivity().getSupportFragmentManager(),"mydc_dialog_test");
                return true;
            }
        });
        tv_week = requireActivity().findViewById(R.id.tv_week);
        tv_date = requireActivity().findViewById(R.id.tv_date);
        tv_address = requireActivity().findViewById(R.id.tv_address);
        tv_battery = requireActivity().findViewById(R.id.tv_battery);

        if (!SETTING_HTTP_SERVER_ENABLE_HTTP_SERVER) {
            tv_address.setVisibility(View.GONE);
        } else {
            if (SETTING_UI_SHOW_SERVER_ADDRESS) {
                tv_address.setVisibility(View.VISIBLE);
            } else {
                tv_address.setVisibility(View.GONE);
            }
        }

        tv_hour.setOnClickListener(this);
        tv_min.setOnClickListener(this);
        tv_address.setOnClickListener(this);
        tv_battery.setOnClickListener(this);

        tv_address.setText("http:/" + NetUtils.getLocalIPAddress() + ":" + SETTING_HTTP_SERVER_PORT);

        NotifyFragment notifyFragment = new NotifyFragment();
        MediaCtrlFragment mediaCtrlFragment = new MediaCtrlFragment();
        TaskListFragment taskListFragment = new TaskListFragment();

        fl_notify = requireActivity().findViewById(R.id.fl_notify);
        fl_media_ctrl = requireActivity().findViewById(R.id.fl_media_ctrl);
        fl_task = requireActivity().findViewById(R.id.fl_task);

        if (!SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL) {
            fl_media_ctrl.setVisibility(View.GONE);
        }


        fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fl_notify, notifyFragment);
        fragmentTransaction.replace(R.id.fl_media_ctrl, mediaCtrlFragment);
        fragmentTransaction.replace(R.id.fl_task, taskListFragment);
        fragmentTransaction.commit();

        myHandler = new MyHandler();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String ACTION = intent.getAction();
                if (ACTION == null) {
                    return;
                }
                switch (ACTION) {
                    case Intent.ACTION_TIME_TICK: {
                        myHandler.sendEmptyMessage(MyHandler.WHAT_TIME);
                        if(SETTING_UI_AUTO_FLASH_SCREEN){
                            FlashDistanceTime--;
                            switch (FlashDistanceTime){
                                case 0:{
                                    FlashDistanceTime=100;
                                }
                                case 99:{
                                    MainActivityLand.flash();
                                }
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
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        requireContext().registerReceiver(broadcastReceiver, intentFilter);

        setTime();
        setBattery();
    }

    private void setTime() {
        Calendar calendar = Calendar.getInstance();

        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        tv_week.setText(String.format("星期%s", TimeUtils.num2Chinese(weekday)));
        tv_hour.setText(Utils.ensure2Numbers(hour));
        tv_min.setText(Utils.ensure2Numbers(min));
        tv_date.setText(TimeUtils.getFormattedTime(System.currentTimeMillis(), TimeUtils.yMdTimeFormat));
    }

    @SuppressLint("SetTextI18n")
    private void setBattery() {
        int level = HardwareUtils.getBatteryLevel();
        tv_battery.setText(level + "%");
    }


    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        private static final int WHAT_TIME = 338;
        private static final int WHAT_BATTERY = 33;

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WHAT_TIME: {
                    setTime();
                    break;
                }
                case WHAT_BATTERY: {
                    setBattery();
                    break;
                }
            }
        }
    }
}
