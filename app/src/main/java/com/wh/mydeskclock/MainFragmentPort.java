package com.wh.mydeskclock;

import android.annotation.SuppressLint;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.wh.mydeskclock.app.settings.SettingActivity;
import com.wh.mydeskclock.utils.HardwareUtils;
import com.wh.mydeskclock.utils.TimeUtils;
import com.wh.mydeskclock.utils.Utils;
import com.wh.mydeskclock.widget.MyDialog;

import java.util.Calendar;

public class MainFragmentPort extends BaseFragment implements View.OnClickListener {
    private TextView tv_hour;
    private TextView tv_min;
    private TextView tv_battery;
    private TextView tv_week;
    private TextView tv_date;
    private BroadcastReceiver broadcastReceiver;
    private MyHandler myHandler;

    MainFragmentPort(){
        super.setTAG("WH_"+getClass().getSimpleName());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_port,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver!=null){
            requireContext().unregisterReceiver(broadcastReceiver);
        }
    }

    private void init(){
        tv_hour = requireActivity().findViewById(R.id.tv_hour);
        tv_min = requireActivity().findViewById(R.id.tv_min);
        tv_week = requireActivity().findViewById(R.id.tv_week);
        tv_date = requireActivity().findViewById(R.id.tv_date);
        tv_battery = requireActivity().findViewById(R.id.tv_battery);

        tv_hour.setOnClickListener(this);
        tv_min.setOnClickListener(this);
        tv_battery.setOnClickListener(this);


        myHandler = new MyHandler();

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String ACTION = intent.getAction();
                switch (ACTION){
                    case Intent.ACTION_TIME_TICK:{
                        myHandler.sendEmptyMessage(MyHandler.WHAT_TIME);
                        break;
                    }
                    case Intent.ACTION_BATTERY_CHANGED:{
                        myHandler.sendEmptyMessage(MyHandler.WHAT_BATTERY);
                        break;
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        requireContext().registerReceiver(broadcastReceiver,intentFilter);

        setTime();
        setBattery();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hour: {
//                Toast.makeText(requireContext(), "run", Toast.LENGTH_SHORT).show();
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

    private void setBattery(){
        tv_battery.setText(HardwareUtils.getBatteryLevel(requireContext()) + "%");
    }


    @SuppressLint("HandlerLeak")
    class MyHandler extends Handler {
        private static final int WHAT_TIME = 338;
        private static final int WHAT_BATTERY = 33;

        @SuppressLint("SetTextI18n")
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
