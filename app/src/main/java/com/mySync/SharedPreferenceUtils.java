package com.mySync;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;

import androidx.preference.PreferenceManager;

public class SharedPreferenceUtils {
    private String TAG = "WH_" + SharedPreferenceUtils.class.getSimpleName();

    private SharedPreferences spf_default;
    private SharedPreferences spf;
    private static final String SharedPreferenceFile = "com.wh.mydeskclock.COAST_REC";

    public SharedPreferenceUtils(Context context) {
        spf_default = PreferenceManager.getDefaultSharedPreferences(context);
        spf = context.getSharedPreferences(SharedPreferenceFile, Context.MODE_PRIVATE);
    }

    private static final String KEY_setting_server_https = "setting_server_https";

    private boolean getSetting_server_https() {
        return spf_default.getBoolean(KEY_setting_server_https, false);
    }


    private static final String KEY_setting_server_address = "setting_server_address";

    private String getSetting_server_address() {
        return spf_default.getString(KEY_setting_server_address, "127.0.0.1");
    }

    private static final String KEY_setting_server_port = "setting_server_port";

    private int getSetting_server_port() {
        return Integer.parseInt(spf_default.getString(KEY_setting_server_port, "5000"));
    }

    private static final String KEY_setting_access_token_key = "setting_access_token_key";

    public String getSetting_access_token_key() {
        return spf_default.getString(KEY_setting_access_token_key, "access_token");
    }

    private static final String KEY_setting_access_token_value = "setting_access_token_value";

    public String getSetting_access_token_value() {
        return spf_default.getString(KEY_setting_access_token_value, "asdf");
    }

    private static final String KEY_setting_dev_name = "setting_dev_name";

    public String getSetting_dev_name() {
        return spf_default.getString(KEY_setting_dev_name, "device");
    }

    private static final String KEY_setting_get_all_notifies_on_start = "setting_get_all_notifies_on_start";

    public boolean isSetting_get_all_notifies_on_start() {
        return spf_default.getBoolean(KEY_setting_get_all_notifies_on_start, true);
    }

    public String getFullServerAddress() {
        String protocol = getSetting_server_https() ? "https" : "http";
        return protocol + "://" + getSetting_server_address() + ":" + getSetting_server_port();
    }


    public int getCOAST_BATTERY() {
        return spf.getInt("COAST_BATTERY", 0);
    }

    public void setCOAST_BATTERY(int COAST_BATTERY) {
        savePre("COAST_BATTERY",COAST_BATTERY);
    }


    public int getCOAST_MINUTE() {
        return spf.getInt("COAST_MINUTE", 0);
    }

    public void setCOAST_MINUTE(int COAST_MINUTE) {
        savePre("COAST_MINUTE",COAST_MINUTE);
    }


    private int COAST_FLASH = 0;

    public int getCOAST_FLASH() {
        return COAST_FLASH = spf.getInt("COAST_FLASH", 0);
    }

    public void setCOAST_FLASH(int COAST_FLASH) {
        savePre("COAST_FLASH",COAST_FLASH);
    }


    public int getCOAST_SWITCH_THEME() {
        return spf.getInt("COAST_SWITCH_THEME", 0);
    }

    public void setCOAST_SWITCH_THEME(int COAST_SWITCH_THEME) {
        savePre("COAST_SWITCH_THEME",COAST_SWITCH_THEME);
    }


    public int getCOAST_SWITCH_LIGHT() {
        return spf.getInt("COAST_SWITCH_LIGHT", 0);
    }

    public void setCOAST_SWITCH_LIGHT(int COAST_SWITCH_LIGHT) {
        savePre("COAST_SWITCH_LIGHT",COAST_SWITCH_LIGHT);
    }


    public int getSTATUS_SCREEN_OR() {
        return spf.getInt("STATUS_SCREEN_OR",ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void setSTATUS_SCREEN_OR(int STATUS_SCREEN_OR) {
        savePre("STATUS_SCREEN_OR",STATUS_SCREEN_OR);
    }
    public int getSTATUS_SCREEN_BRIGHTNESS() {
        return spf.getInt("STATUS_SCREEN_BRIGHTNESS",0);
    }

    public void setSTATUS_SCREEN_BRIGHTNESS(int STATUS_SCREEN_BRIGHTNESS) {
        savePre("STATUS_SCREEN_BRIGHTNESS",STATUS_SCREEN_BRIGHTNESS);
    }


    public int getSTATUS_BACKGROUND_COLOR() {
        return spf.getInt("STATUS_BACKGROUND_COLOR",Color.WHITE);
    }

    public void setSTATUS_BACKGROUND_COLOR(int STATUS_BACKGROUND_COLOR) {
        savePre("STATUS_BACKGROUND_COLOR",STATUS_BACKGROUND_COLOR);
    }

    private void savePre(String KEY,int VALUE){
        SharedPreferences.Editor editor = spf.edit();
        editor.putInt(KEY,VALUE);
        editor.apply();
    }
}
