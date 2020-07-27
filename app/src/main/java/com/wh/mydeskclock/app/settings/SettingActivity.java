package com.wh.mydeskclock.app.settings;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wh.mydeskclock.BaseActivity;
import com.wh.mydeskclock.R;
import com.wh.mydeskclock.utils.UiUtils;

import java.util.ArrayList;

public class SettingActivity extends BaseActivity {
    Fragment[] fragments;
    ArrayList<View> views;
    int currentFragmentId = 0;

    String TAG = "WH_" + SettingActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        UiUtils.setFullScreen(getWindow());

        fragments = new Fragment[]{
                new SettingUiFragment(),
                new SettingTaskFragment(),
//                new SettingNotifyFragment(),
                new SettingMediaCtrlFragment(),
                new SettingHttpServerFragment(),
                new SettingAboutFragment()};

        views = new ArrayList<>();

        LinearLayout ll_setting_cap = findViewById(R.id.ll_setting_cap);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fl_set, fragments[0]).commit();

        final String[] setting_caps = new String[]{
                "Ui",
                "Task",
//                "Notify",
                "MediaCtrl",
                "HttpServer",
                "About"};
        for (int i = 0; i < setting_caps.length; i++) {
            @SuppressLint("InflateParams")
            View view = LayoutInflater.from(this).inflate(R.layout.item_setting_cap, null, false);
            final int finalI = i;

            TextView textView = view.findViewById(R.id.tv_cap);
            textView.setText(setting_caps[finalI]);

            if (i == 0) {
                setBlackB(view, textView);
            }

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int ii = 0; ii < setting_caps.length; ii++) {
                        if (ii == finalI) {
                            setBlackB(views.get(ii), (TextView) views.get(ii).findViewById(R.id.tv_cap));
                        } else {
                            setWhiteB(views.get(ii), (TextView) views.get(ii).findViewById(R.id.tv_cap));
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.fl_set, fragments[finalI]).commit();
                        }
                    }
                    currentFragmentId = finalI;
                }
            });
            views.add(view);
            ll_setting_cap.addView(view);
        }


    }

    private void setWhiteB(View view, TextView tv_cap) {
        if (view.isEnabled()) {
            view.setBackgroundColor(Color.WHITE);
            tv_cap.setTextColor(Color.BLACK);
        }
    }

    private void setBlackB(View view, TextView tv_cap) {
        if (view.isEnabled()) {
            view.setBackgroundColor(Color.BLACK);
            tv_cap.setTextColor(Color.WHITE);
        }
    }
}