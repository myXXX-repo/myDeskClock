package com.wh.mydeskclock.utils;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.wh.mydeskclock.BaseApp;

import java.util.Objects;

public class Utils {
    /**
     * @describe 将一位的数值转换成两位
     * @args ...
     * @return Void null
     * */
    public static String ensure2Numbers(int num) {
        if (num >= 0 && num < 10) {
            return "0" + num;
        } else {
            return String.valueOf(num);
        }
    }

    public static void pf_coast_int_add(String key){
        SharedPreferences.Editor editor = BaseApp.sp_COAST.edit();
        int TMP = Integer.parseInt(Objects.requireNonNull(BaseApp.sp_COAST.getString(key, "0")));
        editor.putString(key, String.valueOf(TMP+=1));
        editor.apply();
    }

    /**
     * @describe log Bundle内容
     * @args ...
     * @return Void null
     * */
    public static void unPackBundle(Bundle bundle,String TAG){
        Log.d(TAG, "unPackBundle: =====================================");
        for (String key : bundle.keySet()) {
            Log.d(TAG, "onReceive: "+key+" "+bundle.get(key));
        }
        Log.d(TAG, "unPackBundle: =====================================");
    }
}
