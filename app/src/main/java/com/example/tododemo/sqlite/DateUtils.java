package com.example.tododemo.sqlite;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// 日期工具类
public class DateUtils {
    //long转为日期
    public static String longToDate(long lo){
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sd.format(date);
    }

    //日期转为long(即毫秒值)
    public static long stringToLong(String time){
        Date date = null;
        try {
            SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
            date = format.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
}
