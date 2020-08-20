package com.wh.mydeskclock;


import android.os.Bundle;
import android.os.PersistableBundle;

import android.view.WindowManager;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;


import com.wh.mydeskclock.utils.UiUtils;


public class BaseActivity extends AppCompatActivity {
    String TAG = "WH_BaseActivity";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        UiUtils.setFullScreen(getWindow());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    @Override
    protected void onStart() {
        super.onStart();
        UiUtils.setFullScreen(getWindow());
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiUtils.setFullScreen(getWindow());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UiUtils.setFullScreen(getWindow());
    }

    @Override
    protected void onStop() {
        super.onStop();
        UiUtils.setFullScreen(getWindow());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    void setTAG(String TAG) {
        this.TAG = TAG;
    }

}
