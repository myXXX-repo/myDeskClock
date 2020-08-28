package com.wh.mydeskclock.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {
    public static final String fullDateTimeFormat = "yyyy-MM-dd HH:mm:ss";
    public static final String yMdTimeFormat = "yyyy-MM-dd";


    /**
     * @describe 将长整型的时间戳转换成"yyyy-MM-dd"格式的时间
     * @args MS long 长整型的毫秒时间戳
     * @return 返回格式化后的字符串
     * */
    public static String getFormattedTime(long MS){
        return genDateFormat(fullDateTimeFormat).format(new Date(MS));
    }

    /**
     * @describe 将长整型的时间戳转换成自定义的格式
     * @args MS long 长整型的毫秒时间戳
     *       format String 格式
     * @return 返回格式化后的字符串
     * */
    public static String getFormattedTime(long MS,String format){
        return genDateFormat(format).format(new Date(MS));
    }

    /**
     * @describe 将格式化的时间戳字符串转换成长整型的毫秒时间戳
     * @args formattedTime String 格式化的时间戳
     * @return Long 返回转换后的长整型时间戳
     * */
    public static long getMSFromFormattedTime(String formattedTime) throws ParseException {
        Date date = genDateFormat(fullDateTimeFormat).parse(formattedTime);
        if (date != null) {
            return date.getTime();
        }else {
            return 0L;
        }
    }

    /**
     * @describe 输入格式字符串,返回格式的数据结构
     * @args stringFormat String 格式字符串
     * @return 返回格式数据结构
     * */
    public static SimpleDateFormat genDateFormat(String stringFormat){
        return new SimpleDateFormat(stringFormat, Locale.CHINA);
    }

    /**
     * @describe 输入星期的日期 返回汉字
     * @args num int 输入的日期
     * @return String 返回日期
     * */
    public static String num2Chinese(int num){
        String[] c = {"六", "日", "一", "二", "三", "四", "五"};
        if (num > -1 && num < c.length) {
            return c[num];
        } else {
            return c[0];
        }
    }
}
