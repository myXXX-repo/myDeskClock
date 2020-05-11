package com.mySync;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

public class SharedPreferenceUtils {
    private String TAG = "WH_"+SharedPreferenceUtils.class.getSimpleName();

    private SharedPreferences spf;
    public SharedPreferenceUtils(Context context) {
        spf = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean setting_server_https;
    public static final String KEY_setting_server_https = "setting_server_https";
    public boolean getSetting_server_https(){
        setting_server_https = spf.getBoolean(KEY_setting_server_https,false);
        return setting_server_https;
    }


    public String setting_server_address;
    public static final String KEY_setting_server_address = "setting_server_address";
    public String getSetting_server_address(){
        setting_server_address = spf.getString(KEY_setting_server_address,"127.0.0.1");
        return setting_server_address;
    }

    public int setting_server_port;
    public static final String KEY_setting_server_port = "setting_server_port";
    public int getSetting_server_port() {
        setting_server_port = Integer.parseInt(spf.getString(KEY_setting_server_port,"5000"));
        return setting_server_port;
    }

    public String setting_access_token_key;
    public static final String KEY_setting_access_token_key = "setting_access_token_key";
    public String getSetting_access_token_key() {
        setting_access_token_key = spf.getString(KEY_setting_access_token_key,"access_token");
        return setting_access_token_key;
    }

    public String setting_access_token_value;
    public static final String KEY_setting_access_token_value = "setting_access_token_value";

    public String getSetting_access_token_value() {
        setting_access_token_value = spf.getString(KEY_setting_access_token_value,"asdf");
        return setting_access_token_value;
    }

    public String setting_dev_name;
    public static final String KEY_setting_dev_name = "setting_dev_name";
    public String getSetting_dev_name() {
        setting_dev_name = spf.getString(KEY_setting_dev_name,"device");
        return setting_dev_name;
    }

    public boolean setting_get_all_notifies_on_start;
    public static final String KEY_setting_get_all_notifies_on_start = "setting_get_all_notifies_on_start";
    public boolean isSetting_get_all_notifies_on_start() {
        setting_get_all_notifies_on_start = spf.getBoolean(KEY_setting_get_all_notifies_on_start,true);
        return setting_get_all_notifies_on_start;
    }


    public String getFullServerAddress(){
        String protocol = getSetting_server_https()?"https":"http";
        return protocol+"://"+getSetting_server_address()+":"+getSetting_server_port();
    }
}
