package com.wh.mydeskclock;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BadParcelableException;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wh.mydeskclock.Utils.HardwareUtils;
import com.wh.mydeskclock.Utils.TimeUtils;
import com.wh.mydeskclock.Utils.UiUtils;
import com.wh.mydeskclock.Utils.Utils;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class ClockFragment extends Fragment {

    private ClockHandler clockHandler;

    private BroadcastReceiver timeReceiver;
    private BroadcastReceiver batteryReceiver;
    private TextView tv_hour;
    private TextView tv_min;
    private TextView tv_week;
    private TextView tv_battery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clockHandler = new ClockHandler();
        timeReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                clockHandler.sendEmptyMessage(ClockHandler.WHAT_TIME);
            }
        };
        IntentFilter timeFilter = new IntentFilter();
        timeFilter.addAction(Intent.ACTION_TIME_TICK);
        requireActivity().registerReceiver(timeReceiver, timeFilter);

        batteryReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                clockHandler.sendEmptyMessage(ClockHandler.WHAT_BATTERY);
            }
        };
        IntentFilter batteryFilter = new IntentFilter();
        batteryFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        requireActivity().registerReceiver(batteryReceiver,batteryFilter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_clock, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tv_hour = requireActivity().findViewById(R.id.tv_hour);
        tv_min = requireActivity().findViewById(R.id.tv_min);
        tv_week = requireActivity().findViewById(R.id.tv_week);
        tv_battery = requireActivity().findViewById(R.id.tv_battery);

        setTime();
        tv_battery.setText(HardwareUtils.getBatteryLevel(requireContext())+"%");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timeReceiver!=null){
            requireActivity().unregisterReceiver(timeReceiver);
        }
        if(batteryReceiver!=null){
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
    }

    @SuppressLint("HandlerLeak")
    class ClockHandler extends Handler{
        private static final int WHAT_TIME = 261;
        private static final int WHAT_BATTERY = 440;
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case WHAT_TIME:{
                    setTime();
                    break;
                }
                case WHAT_BATTERY:{
                    tv_battery.setText(HardwareUtils.getBatteryLevel(requireContext())+"%");
                    break;
                }
            }
        }
    }
}