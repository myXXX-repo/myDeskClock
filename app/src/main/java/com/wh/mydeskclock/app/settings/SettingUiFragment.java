package com.wh.mydeskclock.app.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.wh.mydeskclock.R;

public class SettingUiFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_ui, rootKey);
    }
}
