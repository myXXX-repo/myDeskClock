package com.wh.mydeskclock;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wh.mydeskclock.utils.SharedPreferenceUtils;
import com.wh.mydeskclock.utils.UiUtils;
import com.wh.mydeskclock.widget.MyDialog;

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
                new SettingNotifyFragment(),
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
                "Notify",
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
    public static class SettingUiFragment extends PreferenceFragmentCompat{

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.setting_ui, rootKey);
        }
    }

    public static class SettingTaskFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.setting_task, rootKey);
        }
    }

    public static class SettingNotifyFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.setting_notify, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(Preference preference) {
            if ("setting_http_server_force_stop".equals(preference.getKey())) {
                Intent intent = new Intent();
                intent.setAction("myDeskClock_server_exit");
                requireContext().sendBroadcast(intent);
                Toast.makeText(requireContext(), "stopping...", Toast.LENGTH_SHORT).show();
            }

            return super.onPreferenceTreeClick(preference);
        }
    }

    public static class SettingMediaCtrlFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.setting_media_ctrl, rootKey);

        }
    }

    public static class SettingHttpServerFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.setting_http_server, rootKey);

        }
    }

    public static class SettingAboutFragment extends PreferenceFragmentCompat{
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.setting_about, rootKey);
        }

        @Override
        public boolean onPreferenceTreeClick(final Preference preference) {
            switch (preference.getKey()){
                case SharedPreferenceUtils.sp_default.SETTING_ABOUT_ME_GITHUB:{}
                case SharedPreferenceUtils.sp_default.SETTING_ABOUT_ME_COOLAPK:{}
                case SharedPreferenceUtils.sp_default.SETTING_ABOUT_GOTO_APP_COOLAPK:{
                    AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                            .setTitle("Navigation Alert")
                            .setMessage("Open URL "+preference.getSummary()+" with browser")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String addr = (String) preference.getSummary();
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(addr));
                                    startActivity(intent);
                                }
                            }).setNegativeButton("No",null);
                    MyDialog myDialog = new MyDialog(builder);
                    myDialog.setFullScreen();
                    myDialog.show(requireActivity().getSupportFragmentManager(),"myDeskClock_open_url_alert");
                }
            }
            return super.onPreferenceTreeClick(preference);
        }
    }
}