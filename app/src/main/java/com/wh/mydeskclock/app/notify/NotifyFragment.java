package com.wh.mydeskclock.app.notify;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wh.mydeskclock.BaseFragment;
import com.wh.mydeskclock.R;
import com.wh.mydeskclock.utils.TimeUtils;

import org.jetbrains.annotations.NotNull;

public class NotifyFragment extends BaseFragment {
    String TAG = "WH_"+NotifyFragment.class.getSimpleName();

    private BroadcastReceiver notifyReceiver;
    private MyHandler myHandler;
    private LinearLayout ll_notify;
    private TextView tv_con;
    private TextView tv_device;
    private TextView tv_title;
    private TextView tv_create_time;
    private View rootView;
    private ImageView iv_close;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myHandler = new MyHandler();
        notifyReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getBundleExtra("extra");
                if(bundle!=null){
                    Message m = Message.obtain();
                    m.what = 1;
                    m.obj = bundle;
                    myHandler.sendMessage(m);
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("showNotify");

        requireContext().registerReceiver(notifyReceiver,intentFilter);
    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = LayoutInflater.from(requireContext()).inflate(R.layout.item_notify,null,false);
        tv_con = rootView.findViewById(R.id.tv_notify_item_con);
        tv_device = rootView.findViewById(R.id.tv_notify_item_device);
        tv_title = rootView.findViewById(R.id.tv_notify_item_title);
        tv_create_time = rootView.findViewById(R.id.tv_notify_item_create_time);
        rootView.setVisibility(View.GONE);

        iv_close = rootView.findViewById(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rootView.setVisibility(View.GONE);
            }
        });

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(notifyReceiver!=null){
            requireContext().unregisterReceiver(notifyReceiver);
        }
    }

    class MyHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:{

                    Bundle bundle = (Bundle) msg.obj;

                    rootView.setVisibility(View.VISIBLE);

                    String DEVICE = bundle.getString("DEVICE");
                    String TITLE = bundle.getString("TITLE");
                    String NOTIFY = bundle.getString("NOTIFY");

                    tv_con.setText(NOTIFY);
                    tv_device.setText(DEVICE);
                    tv_title.setText(TITLE);
                    tv_create_time.setText(TimeUtils.getFormattedTime(System.currentTimeMillis()));

                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(requireContext(), notification);
                    r.play();

                    new Thread(){
                        @Override
                        public void run() {
                            rootView.setBackgroundColor(Color.BLACK);
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            rootView.setBackgroundColor(Color.WHITE);
                        }
                    }.start();
                    break;
                }
            }
        }
    }
}