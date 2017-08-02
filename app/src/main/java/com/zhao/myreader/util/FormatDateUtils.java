package com.zhao.myreader.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by hmt on 2016/12/7.
 */

public class FormatDateUtils {
    private static Date date;
    private static SimpleDateFormat format;

    private static SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        return mDateFormat.format(new Date(time));
    }

    public static String long2date(String str, String formater){
        return formateLongTime(long2String(str),formater);
    }

    public static String long2date(long times, String formater){
        return formateLongTime(times,formater);
    }


    private static String formateLongTime(long times, String formater){
        date=new Date(times);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        format = new SimpleDateFormat(formater);
        String sb=format.format(gc.getTime());
        return sb;
    }



    public static long long2String(String str){
        long time = 0;
        try {
            time = mDateFormat.parse(str).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    public static long day2long(long times){
        long day = times / (1000*60*60*24);
        return day;
    }

}
