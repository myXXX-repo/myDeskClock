package com.wh.mydeskclock.utils;

import android.content.SharedPreferences;

import com.wh.mydeskclock.App;

import java.util.Objects;

public class Utils {
    public static String ensure2Numbers(int num) {
        if (num >= 0 && num < 10) {
            return "0" + num;
        } else {
            return String.valueOf(num);
        }
    }

    public static void pf_coast_int_add(String key){
        SharedPreferences.Editor editor = App.sp_COAST.edit();
        int TMP = Integer.parseInt(Objects.requireNonNull(App.sp_COAST.getString(key, "0")));
        editor.putString(key, String.valueOf(TMP+=1));
        editor.apply();
    }
}
