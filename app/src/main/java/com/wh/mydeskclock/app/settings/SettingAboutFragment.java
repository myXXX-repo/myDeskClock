package com.wh.mydeskclock.app.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.wh.mydeskclock.Config;
import com.wh.mydeskclock.R;

public class SettingAboutFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_about, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()){
            case Config.DefaultSharedPreferenceKey.SETTING_ABOUT_ME_GITHUB:{}
            case Config.DefaultSharedPreferenceKey.SETTING_ABOUT_ME_COOLAPK:{}
            case Config.DefaultSharedPreferenceKey.SETTING_ABOUT_GOTO_APP_COOLAPK:{
                String addr = (String) preference.getSummary();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(addr));
                startActivity(intent);
            }
        }
        return super.onPreferenceTreeClick(preference);
    }
}
