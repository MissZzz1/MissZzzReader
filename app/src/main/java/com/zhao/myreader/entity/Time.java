package com.zhao.myreader.entity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by zhao on 2016/12/7.
 */

public class Time {
    private int year;
    private int month;
    private int date;
    private int hour;
    private int minute;
    private int second;

    public long getLongTime(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(year);
        if(month < 10){
            stringBuilder.append("0"+month);
        }else {
            stringBuilder.append(month);
        }
        if(date < 10){
            stringBuilder.append("0" + date);
        }else {
            stringBuilder.append(date);
        }
        if(hour < 10){
            stringBuilder.append("0" + hour);
        }else {
            stringBuilder.append(hour);
        }
        if(minute < 10){
            stringBuilder.append("0" + minute);
        }else {
            stringBuilder.append(minute);
        }
        if(second < 10){
            stringBuilder.append("0" + second);
        }else {
            stringBuilder.append(second);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        java.util.Date date = null;
        try {
            date = sdf.parse(stringBuilder.toString());
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public void init(Date date){
        year = date.getYear() + 1900;
        month = date.getMonth() + 1;
        this.date = date.getDate();
        hour = date.getHours();
        minute = date.getMinutes();
        second = 0;
    }

    public void init(com.zhao.myreader.entity.Date date){
        Date date1 = new Date();
        year = date.getYear();
        month = date.getMonth();
        this.date = date.getDate();
        hour = date1.getHours();
        minute = date1.getMinutes();
        second = 0;
    }

    public void setToDayZero(){
        hour = 0;
        minute = 0;
        second = 0;
    }

    public void setToDayLast(){
        hour = 23;
        minute = 59;
        second = 59;
    }

    public void lastMonth(){
        if(month == 1){
            month = 12;
            year = year - 1;
        }else {
            month = month - 1;
        }
    }

    public void nextMonth(){
        if(month == 12){
            month = 1;
            year = year + 1;
        }else {
            month = month + 1;
        }
    }

    public void init(long time){
        Date date = new Date(time);
        init(date);
    }


    public int getDate() {
        return date;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getMonth() {
        return month;
    }

    public int getSecond() {
        return second;
    }

    public int getYear() {
        return year;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
