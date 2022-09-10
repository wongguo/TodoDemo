package com.example.tododemo.sqlite;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    //long转为日期
    public static String longToDate(long lo){
        Date date = new Date(lo);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        return sd.format(date);
    }

    //日期转为long
    public static long stringToLong(String time){
        Date date = null;
        try {
            SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd");
            date = format.parse(time);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }

    }
}
