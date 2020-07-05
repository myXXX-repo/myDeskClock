package com.wh.mydeskclock;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class ClockViewModel extends AndroidViewModel {
//    private Context context;
    private SharedPreferences sharedPreferences;
    public ClockViewModel(@NonNull Application application) {
        super(application);
//        context = application.getApplicationContext();
        sharedPreferences = application.getSharedPreferences(Config.SharedPreferenceVALUE.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
    }
}
