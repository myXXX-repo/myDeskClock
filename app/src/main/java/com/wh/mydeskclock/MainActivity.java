package com.wh.mydeskclock;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wh.mydeskclock.Utils.UiUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiUtils.setFullScreen(getWindow());
    }

    @Override
    protected void onResume() {
        super.onResume();
        UiUtils.setFullScreen(getWindow());
    }


}