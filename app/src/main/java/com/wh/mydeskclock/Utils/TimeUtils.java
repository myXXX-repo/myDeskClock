package com.wh.mydeskclock.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    private static final String fullDateTimeFormat = "yyyy-MM-dd HH:mm:ss";

    public static String getFormattedTime(long MS){
        return genDateFormat(fullDateTimeFormat).format(new Date(MS));
    }

    public static long getMSFromFormattedTime(String formattedTime) throws ParseException {
        Date date = genDateFormat(fullDateTimeFormat).parse(formattedTime);
        if (date != null) {
            return date.getTime();
        }else {
            return 0L;
        }
    }

    public static SimpleDateFormat genDateFormat(String stringFormat){
        return new SimpleDateFormat(stringFormat, Locale.CHINA);
    }

    public static String num2Chinese(int num){
        String[] c = {"六", "日", "一", "二", "三", "四", "五"};
        if (num > -1 && num < c.length) {
            return c[num];
        } else {
            return c[0];
        }
    }
}
