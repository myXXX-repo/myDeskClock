package com.wh.mydeskclock;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.wh.mydeskclock.Utils.UiUtils;

public class MainActivity extends AppCompatActivity {
    String TAG= "WH_"+MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiUtils.setFullScreen(getWindow());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        UiUtils.setFullScreen(getWindow());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        UiUtils.setFullScreen(getWindow());
    }


}