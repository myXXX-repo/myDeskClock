package com.wh.mydeskclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wh.mydeskclock.Utils.UIUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UIUtils.setFullScreen(getWindow()).start();
    }
}