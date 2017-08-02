package com.zhao.myreader.util;



import com.zhao.myreader.entity.Date;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by zhao on 2016/11/24.
 */

public class CalendarHelper {

    public static ArrayList<Date> getMonthDate(int year, int month) {
        int totalDays = 0, curMonthDays = 0, firstDayOfMonth = 0, lastMonthDays;
        ArrayList<Date> dates = new ArrayList<>();
        curMonthDays = getMonthDays(year, month);
        for (int i = 1900; i < year; i++) {
            if ((i % 4 == 0 && i % 100 != 0) || (i % 400 == 0)) {
                totalDays = totalDays + 366;
            } else {
                totalDays = totalDays + 365;
            }
        }
        for (int i = 1; i < month; i++) {
            totalDays += getMonthDays(year, i);
        }

        int temp = 1 + totalDays % 7;
        if (temp == 7) {
            firstDayOfMonth = 0; //周日
        } else {
            firstDayOfMonth = temp;
        }
        if (month != 1) {
            lastMonthDays = getMonthDays(year, month - 1);
        } else {
            lastMonthDays = getMonthDays(year - 1, 12);
        }
        for (int i = 0; i < firstDayOfMonth; i++) {
            Date date = new Date();
            if(month == 1){
                date.setYear(year - 1);
                date.setMonth(12);
            }else {
                date.setYear(year);
                date.setMonth(month -1);
            }
            date.setDate(lastMonthDays - firstDayOfMonth + i + 1);
            date.setDay(i);
            dates.add(date);
        }
        for(int i = 0; i < curMonthDays; i++ ){
            Date date = new Date();
            date.setYear(year);
            date.setMonth(month);
            date.setDate(i + 1);
            date.setDay(firstDayOfMonth ++);
            if(firstDayOfMonth > 6){
                firstDayOfMonth = 0;
            }
            dates.add(date);
        }
        if(firstDayOfMonth != 0){
            int tem = 1;
            for(int i = firstDayOfMonth; i < 7; i++){
                Date date = new Date();
                if(month == 12){
                    date.setYear(year + 1);
                    date.setMonth(1);
                }else {
                    date.setYear(year);
                    date.setMonth(month +1);
                }
                date.setDate(tem++);
                date.setDay(i);
                dates.add(date);
            }
        }
        return dates;
    }

    public static int getMonthDays(int year,int month){
        boolean isRun = false;
        if((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)){
            isRun = true;
        }
        switch(month){
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;

            case 2:
                if(isRun){
                    return 29;
                }else{
                    return 28;
                }
            default:
               return 0;
        }
    }

    private static Calendar dateToCalendar(Date date){
        java.util.Date date1 = new java.util.Date(date.getYear()-1900,date.getMonth()-1,date.getDate());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date1);
        return calendar;
    }

    public static Date getDate(java.util.Date date){
        Date dateRes = new Date();
        dateRes.setDate(date.getDate());
        dateRes.setYear(date.getYear() + 1900);
        dateRes.setMonth(date.getMonth() + 1);
        dateRes.setDay(date.getDay());
        return dateRes;
    }

    public static boolean isTomorrow(Date date){
        Date today = getDate(new java.util.Date());
        if(today.nextDate().isSameDate(date)){
            return true;
        }else return false;
    }



    public static String getDay(Date date) {
        String[] weekDaysName = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        String[] weekDaysCode = { "0", "1", "2", "3", "4", "5", "6" };
        Calendar calendar = dateToCalendar(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysCode[intWeek];
    }

    public static String getDate(Date date) {
        StringBuilder stringBuilder = new StringBuilder();
        if (date.getMonth() < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(String.valueOf(date.getMonth()));
        stringBuilder.append(".");
        if (date.getDate() < 10) {
            stringBuilder.append("0");
        }
        stringBuilder.append(String.valueOf(date.getDate()));
        return stringBuilder.toString();
    }

    public static String getDay2(Date date) {
        Date today = CalendarHelper.getDate(new java.util.Date());
        if (date.isSameDate(today)) {
            return "今天";
        } else if (CalendarHelper.isTomorrow(date)) {
            return "明天";
        } else if (CalendarHelper.isTomorrow(date.lastDate())) {
            return "后天";
        } else {
            switch (date.getDay()) {
                case 0:
                    return "周日";

                case 1:
                    return "周一";

                case 2:
                    return "周二";

                case 3:
                    return "周三";

                case 4:
                    return "周四";

                case 5:
                    return "周五";

                case 6:
                    return "周六";

            }
        }
        return "";
    }



    /**
     * 获取未来 任意天内的日期数组
     * @param intervals      intervals天内
     * @return              日期数组
     */
    public static ArrayList<Date> getFetureDates(int intervals) {
//        ArrayList<Date> pastDaysList = new ArrayList<>();
        ArrayList<Date> fetureDaysList = new ArrayList<>();
        Date date = CalendarHelper.getDate(new java.util.Date());
        for (int i = 0; i <intervals; i++) {
//            pastDaysList.add(getPastDate(i));
            fetureDaysList.add(date);
            date = date.nextDate();
        }
        return fetureDaysList;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static Date getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        java.util.Date today = calendar.getTime();
        return getDate(today);
    }

    /**
     * 获取未来 第 past 天的日期
     * @param past
     * @return
     */
    public static Date getFetureDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        java.util.Date today = calendar.getTime();
        return getDate(today);
    }


}
