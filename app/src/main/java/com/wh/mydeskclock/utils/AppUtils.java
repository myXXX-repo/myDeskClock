package com.wh.mydeskclock.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class AppUtils {

    public static String getAppVersionName(Context context) throws PackageManager.NameNotFoundException {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
        return pi.versionName;
    }

    public static void restartApp(Context BaseContext, Context context) {
        Intent intent = BaseContext.getPackageManager().getLaunchIntentForPackage(BaseContext.getPackageName());
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    /**
     * @return boolean haveNewVersion
     */
    public static boolean checkUpdate(String TAG, Context context) throws PackageManager.NameNotFoundException, IOException {
        String currentVersion = getAppVersionName(context);
        Log.d(TAG, "current version: " + currentVersion);
        String newestVersion = "";
        Document document = Jsoup.connect("https://www.coolapk.com/apk/260552").get();
        String title = document.title();
        String[] ti = title.split("[ ]");
        newestVersion = ti[2];
        Log.d(TAG, "newest version: " + newestVersion);
        return !currentVersion.equals(newestVersion);
    }
}
