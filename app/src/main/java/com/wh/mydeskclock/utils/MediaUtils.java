package com.wh.mydeskclock.utils;

import android.app.Instrumentation;

public class MediaUtils {

    public static void pausePlay() {
        sendKeyCode(85);
    }

    public static void nextPlay() {
        sendKeyCode(87);
    }

    public static void previousPlay() {
        sendKeyCode(88);
    }

    /**
     * @describe 发送键码
     * @args keyCode int 输入键值
     * */
    public static void sendKeyCode(final int KeyCode){
        new Thread() {
            @Override
            public void run() {
                Instrumentation instrumentation = new Instrumentation();
                instrumentation.sendKeyDownUpSync(KeyCode);
            }
        }.start();
    }
}
