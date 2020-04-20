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
//        if(hasFocus){
//            Utils.setWindowSystemUiVisibility(this);
//        }
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

//        frameLayout_con = findViewById(R.id.frameLatout_con);
//        frameLayout_con.setSystemUiVisibility(Utils.getHideSystemUIFlags());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
////        setWindowSystemUiVisibility();
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.d(TAG,"onResume");
////        Utils.setWindowSystemUiVisibility(this);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

//    // 设置窗口系统显示 主要用于全屏--隐藏标题栏 状态栏 动作栏
//    private void setWindowSystemUiVisibility(){
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
//    }

}
