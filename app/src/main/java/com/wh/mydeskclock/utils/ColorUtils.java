package com.wh.mydeskclock.utils;

import android.graphics.Color;

public class ColorUtils {
    public static int transBetweenBW(int COLOR){
        return (COLOR == Color.BLACK)?Color.WHITE:Color.BLACK;
    }
}
