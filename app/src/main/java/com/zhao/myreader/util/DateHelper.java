package com.zhao.myreader.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhao on 2016/9/9.
 */
public class DateHelper {

    /**
     * @param
     * @return
     *            ==>1471941967364
     *               2147483647
     */
    public static long getLongDate(){
        long res = System.currentTimeMillis();
      return res;

    }

    /**
     *
     * @param date==>2016/05/02 09:10:46
     * @return ==>2016/05/02 09:10
     */
   public static String formatDate_3(String date){
        String res = date;
        res = res.substring(0,res.length()-3);
        return res;
    }

    /**
     *截断字符串末尾num个字符
     */
    public static String formatDateByTailNum(String date, int num){
        String res = date;
        res = res.substring(0,res.length() - num);
        return res;
    }
    /**
     *
     * @param date==>2016/05/02 09:10:46
     * @return ==>2016/05/02
     */
    public static String formatDate3(String date){
        String res = date;
        res = res.substring(0,res.length() - 9);
        return res;
    }


    public static String getStrLongDate(){
       return String.valueOf(getLongDate());
    }

    public static Date longToDate(long longDate){

        Date date = new Date(longDate);
        return date;

    }

    public static Date strLongToDate(String strLongDate){
        Date date = null;
        try{
            date = new Date(Long.parseLong(strLongDate));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return date;
    }

    /**
     *
     * @param longDate ==>1471941967364
     * @return ==>2016/05/02 09:10:46
     */
    public static String longToTime(long longDate){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strTime = df.format(longToDate(longDate));
        return strTime;
    }

    /**
     *
     * @param longDate ==>1471941967364
     * @return ==>2016/05/02 09:10
     */
    public static String longToTime2(long longDate){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String strTime = df.format(longToDate(longDate));
        return strTime;
    }

    /**
     *
     * @param longDate ==>1471941967364
     * @return ==>2016/05/02
     */
    public static String longToTime3(long longDate){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String strTime = df.format(longToDate(longDate));
        return strTime;
    }

    /**
     *
     * @param longDate ==>1471941967364
     * @return ==>09:10
     */
    public static String longToDayTime(long longDate){
        DateFormat df = new SimpleDateFormat("HH:mm");
        String strTime = df.format(longToDate(longDate));
        return strTime;
    }

    /**
     *
     * @param strLongDate  ==>1471941967364
     * @return   ==>2016/05/02 09:10:46
     */
    public static String strLongToTime(String strLongDate){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strTime = df.format(strLongToDate(strLongDate));
        return strTime;
    }

    /**
     *
     * @param longDate ==>1471941967364
     * @return ==>2016年05月02日 09:10
     */
    public static String strLongToScheduleTime(long longDate){
        DateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String strTime = df.format(longToDate(longDate));
        return strTime;
    }


    /**
     * @param
     * @return
     *            ==>Wed May 02 09:10:46 CST 2012
     */
    public static String getTime1(){

        Date date1 = new Date();
        return date1.toString();
    }
    /**
     * @param
     * @return
     *            ==>2012-05-02
     */
    public static String getYearMonthDay1(){

        java.sql.Date date2 = new java.sql.Date(System.currentTimeMillis());
        return date2.toString();
    }
    /**
     * @param
     * @return
     *            ==>2012-05-02
     */
    public static String getYearMonthDay2(){
        Date date1 = new Date();
        java.sql.Date date3 = new java.sql.Date(date1.getTime());
        return date3.toString();
    }
    /**
     * @param
     * @return
     *            ==>2012-05-02 09:10:46.436
     */
    public static String getSeconds1(){
        Timestamp stamp1 = new Timestamp(System.currentTimeMillis());
        return stamp1.toString();
    }

    /**
     * @param
     * @return
     *            ==>2012-05-02 09:10:46.436
     */
    public static String getSeconds2(){

        Date date1 = new Date();
        Timestamp stamp2 = new Timestamp(date1.getTime());
        return stamp2.toString();
    }
    /**
     * @param
     * @return
     *            ==>2012/05/02 09:10:46
     */
    public static String getYear_Second1(){

        Timestamp stamp1 = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = sdf.format(stamp1);
        return timeStr;
    }
    /**
     * @param
     * @return
     *            ==>2012/05/02 09:10:46
     */
    public static String getYear_Second2(){

        Date date1 = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr2 = df.format(date1);
        return timeStr2;
    }
    /**
     * �
     * @param str
     *            ==>yyyy-MM-dd HH:mm:ss
     * @param
     * @return
     *            ==>Thu Dec 10 05:12:02 CST 2009
     */
    public static Date changeStringToDate1(String str){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(str);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     *
     *
     * @param str
     *            ==>yyyy-MM-dd HH:mm:ss
     * @param
     * @return
     *            ==>2009-12-10
     */
    public static String changeStringToDate2(String str){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date6 = null;
        try {
            date6 = sdf.parse(str);
            java.sql.Date date7 = new java.sql.Date(date6.getTime());
            return date7.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     *
     *
     * @param str
     *           ==>yyyy-MM-dd HH:mm:ss
     * @param
     * @return
     *            ==>2009-12-10 05:12:02.0
     */
    public static String changeStringToDate3(String str){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date6 = null;
        try {
            date6 = sdf.parse(str);
            java.sql.Date date7 = new java.sql.Date(date6.getTime());
            Timestamp stamp9 = new Timestamp(date7.getTime());
            return stamp9.toString();
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     *
     * @param str ==>2016/05/02 09:10:46
     * @return 1471941967364
     */

    public static long strDateToLong(String str){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date6 = null;
        try {
            date6 = sdf.parse(str);
            return date6.getTime();
        }catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


}
