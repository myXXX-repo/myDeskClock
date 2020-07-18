package com.wh.mydeskclock.app.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.wh.mydeskclock.Config;
import com.wh.mydeskclock.widget.MyDialog;
import com.wh.mydeskclock.R;

public class SettingAboutFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_about, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(final Preference preference) {
        switch (preference.getKey()){
            case Config.DefaultSharedPreferenceKey.SETTING_ABOUT_ME_GITHUB:{}
            case Config.DefaultSharedPreferenceKey.SETTING_ABOUT_ME_COOLAPK:{}
            case Config.DefaultSharedPreferenceKey.SETTING_ABOUT_GOTO_APP_COOLAPK:{
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
