package com.wh.mydeskclock;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wh.mydeskclock.app.mediaCtrl.MediaCtrlFragment;
import com.wh.mydeskclock.app.notify.NotifyFragment;
import com.wh.mydeskclock.app.settings.SettingActivity;
import com.wh.mydeskclock.app.task.TaskListFragment;
import com.wh.mydeskclock.utils.HardwareUtils;
import com.wh.mydeskclock.utils.NetUtils;
import com.wh.mydeskclock.utils.QRCodeGenerator;
import com.wh.mydeskclock.utils.TimeUtils;
import com.wh.mydeskclock.utils.Utils;

import java.util.Calendar;

public class MainFragment extends Fragment {

    private UiHandler uiHandler;

    private BroadcastReceiver timeReceiver;
    private BroadcastReceiver batteryReceiver;
    private TextView tv_hour;
    private TextView tv_min;
    private TextView tv_week;
    private TextView tv_battery;
    private TextView tv_date;
    private TextView tv_address;

    private MainActivity mParent;
    private MainActivity.MyHandler myHandler;
    private String url = "http:/" + NetUtils.getLocalIPAddress() + ":" + AppConfig.port;
    private SharedPreferences sharedPreferences;
    private boolean SETTING_UI_SHOW_SERVER_ADDRESS;
    private boolean SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL;
    private boolean SETTING_UI_LAND;
    private FrameLayout fl_media_ctrl;
    private FrameLayout fl_notify;
    private FrameLayout fl_task;
    private boolean SETTING_TASK_HIDE_DONE;
    private FragmentManager fragmentManager;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mParent = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 获取需要的设置项目
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        SETTING_UI_SHOW_SERVER_ADDRESS = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_SHOW_SERVER_ADDRESS, true);
        SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL, true);
        SETTING_UI_LAND = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_LAND, true);
        SETTING_TASK_HIDE_DONE = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_TASK_HIDE_DONE,true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 获取界面中的元素
        tv_hour = requireActivity().findViewById(R.id.tv_hour);
        tv_hour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myHandler = new MainActivity.MyHandler(mParent);
                MainActivity.flashScreen(myHandler);
            }
        });
        tv_min = requireActivity().findViewById(R.id.tv_min);
        tv_min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireContext().startActivity(new Intent(requireActivity(), SettingActivity.class));
            }
        });
        tv_week = requireActivity().findViewById(R.id.tv_week);
        tv_date = requireActivity().findViewById(R.id.tv_date);
        // 获取界面元素后 立即更新时间
        setTime();

        // 获取界面并设置电量数值
        tv_battery = requireActivity().findViewById(R.id.tv_battery);
        tv_battery.setText(HardwareUtils.getBatteryLevel(requireContext()) + "%");

        // 获取并处理屏幕底部的地址
        tv_address = requireActivity().findViewById(R.id.tv_address);
        if (tv_address != null) {
            if (SETTING_UI_SHOW_SERVER_ADDRESS) {
                tv_address.setVisibility(View.VISIBLE);
            } else {
                tv_address.setVisibility(View.GONE);
            }
            tv_address.setText(url);
            tv_address.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView iv = new ImageView(requireContext());
                    iv.setImageBitmap(new QRCodeGenerator(url, 500, 500).getQRCode());
                    iv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivityForResult(intent, 0);
                        }
                    });

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(requireContext())
                            .setView(iv);
                    MyDialog myDialog = new MyDialog(alertDialog);
                    myDialog.setFullScreen();
                    myDialog.show(requireActivity().getSupportFragmentManager(), "myDeskClock_address");
                }
            });
        }

        fl_notify = requireActivity().findViewById(R.id.fl_notify);
        fl_media_ctrl = requireActivity().findViewById(R.id.fl_media_ctrl);
        fl_task = requireActivity().findViewById(R.id.fl_task);

        if (fl_notify != null) {
            fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fl_notify, new NotifyFragment());

            fragmentTransaction.add(R.id.fl_media_ctrl, new MediaCtrlFragment());
            if (SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL) {
                fl_media_ctrl.setVisibility(View.VISIBLE);
            } else {
                fl_media_ctrl.setVisibility(View.GONE);
            }

            fragmentTransaction.add(R.id.fl_task, new TaskListFragment());
            fragmentTransaction.commit();
        }


        uiHandler = new UiHandler();

        timeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                uiHandler.sendEmptyMessage(UiHandler.WHAT_TIME);
            }
        };
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        requireActivity().registerReceiver(timeReceiver, timeFilter);

        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                uiHandler.sendEmptyMessage(UiHandler.WHAT_BATTERY);
            }
        };
        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        requireActivity().registerReceiver(batteryReceiver, batteryFilter);

    }

    @Override
    public void onResume() {
        super.onResume();
        SETTING_UI_SHOW_SERVER_ADDRESS = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_UI_SHOW_SERVER_ADDRESS, true);
        SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL, true);
        if (SETTING_UI_SHOW_SERVER_ADDRESS) {
            if (tv_address != null) {
                tv_address.setVisibility(View.VISIBLE);
            }
        } else {
            if (tv_address != null) {
                tv_address.setVisibility(View.GONE);
            }
        }
        if (SETTING_MEDIA_CTRL_ENABLE_MEDIA_CTRL) {
            if (fl_media_ctrl != null) {
                fl_media_ctrl.setVisibility(View.VISIBLE);
            }
        } else {
            if (fl_media_ctrl != null) {
                fl_media_ctrl.setVisibility(View.GONE);
            }
        }
        boolean SETTING_TASK_HIDE_DONE_TMP = sharedPreferences.getBoolean(Config.DefaultSharedPreferenceKey.SETTING_TASK_HIDE_DONE, true);
        if (SETTING_TASK_HIDE_DONE != SETTING_TASK_HIDE_DONE_TMP) {
            SETTING_TASK_HIDE_DONE = SETTING_TASK_HIDE_DONE_TMP;
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fl_task,new TaskListFragment()).commit();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timeReceiver != null) {
            requireActivity().unregisterReceiver(timeReceiver);
        }
        if (batteryReceiver != null) {
            requireActivity().unregisterReceiver(batteryReceiver);
        }
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

    @SuppressLint("HandlerLeak")
    class UiHandler extends Handler {
        private static final int WHAT_TIME = 261;
        private static final int WHAT_BATTERY = 440;

        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WHAT_TIME: {
                    setTime();
                    break;
                }
                case WHAT_BATTERY: {
                    if (tv_battery != null) {
                        tv_battery.setText(HardwareUtils.getBatteryLevel(requireContext()) + "%");
                    }
                    break;
                }
            }
        }
    }
}