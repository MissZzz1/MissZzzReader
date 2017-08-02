package com.zhao.myreader.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.zhao.myreader.common.APPCONST;

import java.util.Date;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Intent.FLAG_INCLUDE_STOPPED_PACKAGES;

/**
 * Created by zhao on 2016/12/9.
 */

public class AlarmHelper {

    private static String alarmActicon = "com.zhao.kl.gxdw";
    private static String AntiHijackingActicon = "com.zhao.kl.gxdw.AntiHijacking";

    public static void addOneShotAlarm(Context context, long time, String msg, int id){
        Date date = new Date();
        if(time <= date.getTime()){
            return;
        }
        Intent intent = new Intent(alarmActicon);
        intent.putExtra(APPCONST.ALARM_SCHEDULE_MSG,msg);
        intent.setFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,time,pi);
        }else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,time,pi);
        }
    }

    public static void removeOneShotAlarm(Context context, int id){
        Intent intent = new Intent(alarmActicon);
        intent.setFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pi);
    }

    public static void addAlarm(Context context, long time, int id){
//        Date date = new Date();
       /* if(time <= date.getTime()){
            return;
        }*/
        Intent intent = new Intent(AntiHijackingActicon);

        intent.setFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,new Date().getTime() + time,pi);
        }else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,new Date().getTime() + time,pi);
        }
    }

    public static void removeAlarm(Context context, int id){
        Intent intent = new Intent(AntiHijackingActicon);
        intent.setFlags(FLAG_INCLUDE_STOPPED_PACKAGES);
        PendingIntent pi = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pi);
    }
}
