package com.wh.mydeskclock.app.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.wh.mydeskclock.R;

public class SettingMediaCtrlFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_media_ctrl, rootKey);

    }
}
