package com.wh.mydeskclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.wh.mydeskclock.app.settings.SettingActivity;
import com.wh.mydeskclock.utils.UiUtils;
import com.wh.mydeskclock.widget.MyDialog;

public class MainFragmentPort extends BaseFragment implements View.OnClickListener {
    private BroadcastReceiver broadcastReceiver;
    private TextView tv_date;
    private TextView tv_battery;

    MainFragmentPort() {
        super.setTAG("WH_" + getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_port, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            requireContext().unregisterReceiver(broadcastReceiver);
        }
    }

    private void init() {
        TextView tv_hour = requireActivity().findViewById(R.id.tv_hour);
        TextView tv_min = requireActivity().findViewById(R.id.tv_min);
        TextView tv_week = requireActivity().findViewById(R.id.tv_week);
        tv_date = requireActivity().findViewById(R.id.tv_date);
        tv_battery = requireActivity().findViewById(R.id.tv_battery);

        tv_hour.setOnClickListener(this);
        tv_min.setOnClickListener(this);
        tv_week.setOnClickListener(this);
        tv_battery.setOnClickListener(this);


        broadcastReceiver = new BatteryTime_BroadcastReceiver(
                new MyHandler(
                        tv_hour,
                        tv_min,
                        tv_week,
                        tv_date,
                        tv_battery));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        requireContext().registerReceiver(broadcastReceiver, intentFilter);

        UiUtils.setTime_MainFragment(tv_hour, tv_min, tv_week, tv_date);
        UiUtils.setBattery_MainFragment(tv_battery);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hour: {
                MainActivityPort.flash();
                break;
            }
            case R.id.tv_min: {
                startActivity(new Intent(requireActivity(), SettingActivity.class));
                break;
            }
            case R.id.tv_battery: {
                AlertDialog.Builder battery_coast_detail = new AlertDialog.Builder(requireContext())
                        .setTitle("Battery Coast Detail")
                        .setMessage("null")
                        .setPositiveButton("Close", null);
                MyDialog myDialog = new MyDialog(battery_coast_detail);
                myDialog.setFullScreen();
                myDialog.show(requireActivity().getSupportFragmentManager(), "battery_coast_detail");
                break;
            }
            case R.id.tv_week: {
                boolean showOthers = tv_date.getVisibility() == View.VISIBLE;
                ImageView iv_battery_ico = requireActivity().findViewById(R.id.iv_battery_ico);
                if (showOthers) {
                    tv_date.setVisibility(View.GONE);
                    tv_battery.setVisibility(View.GONE);
                    iv_battery_ico.setVisibility(View.GONE);
                }else {
                    tv_date.setVisibility(View.VISIBLE);
                    tv_battery.setVisibility(View.VISIBLE);
                    iv_battery_ico.setVisibility(View.VISIBLE);
                }
                break;
            }
        }
    }


    static class MyHandler extends Handler {
        private static final int WHAT_TIME = 338;
        private static final int WHAT_BATTERY = 33;
        private TextView tv_hour;
        private TextView tv_min;
        private TextView tv_week;
        private TextView tv_date;
        private TextView tv_battery;

        public MyHandler(TextView tv_hour, TextView tv_min, TextView tv_week, TextView tv_date, TextView tv_battery) {
            this.tv_hour = tv_hour;
            this.tv_min = tv_min;
            this.tv_week = tv_week;
            this.tv_date = tv_date;
            this.tv_battery = tv_battery;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case WHAT_TIME: {
                    UiUtils.setTime_MainFragment(tv_hour, tv_min, tv_week, tv_date);
                    break;
                }
                case WHAT_BATTERY: {
                    UiUtils.setBattery_MainFragment(tv_battery);
                    break;
                }
            }
        }
    }

    private static class BatteryTime_BroadcastReceiver extends BroadcastReceiver {
        MyHandler myHandler;

        public BatteryTime_BroadcastReceiver(MyHandler myHandler) {
            this.myHandler = myHandler;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String ACTION = intent.getAction();
            switch (ACTION) {
                case Intent.ACTION_TIME_TICK: {
                    myHandler.sendEmptyMessage(MyHandler.WHAT_TIME);
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
