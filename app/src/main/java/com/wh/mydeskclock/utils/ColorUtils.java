package com.wh.mydeskclock.utils;

import android.graphics.Color;

public class ColorUtils {

    /**
     * @describe 黑色白色互换
     *
     * */
    public static int transBetweenBW(int COLOR){
        return (COLOR == Color.BLACK)?Color.WHITE:Color.BLACK;
    }
}
