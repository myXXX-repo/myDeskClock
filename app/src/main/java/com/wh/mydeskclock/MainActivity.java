package com.wh.mydeskclock;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.wh.mydeskclock.Widget.MyDialog;


public class MainActivity extends AppCompatActivity {
    private String TAG = "WH_MainActivity";
    private FrameLayout frameLayout_con;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        Utils.setWindowSystemUiVisibility(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!Utils.Check(this, Utils.NONE_TEST)) {
            Toast.makeText(MainActivity.this, "证书已过期，请到酷安更新", Toast.LENGTH_LONG).show();
            new Thread("quit") {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(3000);
                        finish();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

}
