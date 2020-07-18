package com.wh.mydeskclock.app.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.wh.mydeskclock.R;

public class SettingNotifyFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.setting_notify, rootKey);
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        switch (preference.getKey()){
            case "setting_http_server_force_stop":{
                Intent intent = new Intent();
                intent.setAction("myDeskClock_server_exit");
                requireContext().sendBroadcast(intent);
                Toast.makeText(requireContext(),"stopping...",Toast.LENGTH_SHORT).show();
                break;
            }
        }

        return super.onPreferenceTreeClick(preference);
    }
}
