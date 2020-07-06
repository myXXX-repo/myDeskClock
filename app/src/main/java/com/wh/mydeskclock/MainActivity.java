package com.wh.mydeskclock;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.wh.mydeskclock.NotifyNode.Notify;
import com.wh.mydeskclock.NotifyNode.NotifyRepository;
import com.wh.mydeskclock.Utils.UiUtils;

public class MainActivity extends AppCompatActivity {
    String TAG= "WH_"+MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UiUtils.setFullScreen(getWindow());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Log.d(TAG, "onCreate: ");
        startService(new Intent(MainActivity.this,MainServerService.class));

//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                NotifyRepository notifyRepository = new NotifyRepository(getApplicationContext());
//                notifyRepository.insertNotifies(new Notify("a","b","c"));
//            }
//        }.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        UiUtils.setFullScreen(getWindow());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}