package com.zhao.myreader.util;

import android.app.NotificationManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhao on 2016/9/5.
 */
public class NotifyHelper {

    private static NotificationManager mNotificationManager;
    private static Map mNotifyId = new HashMap();
    private static ArrayList<String> mCurrentConvIds = new ArrayList<String>();

    public static void init(Context context){
        mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

    }

    public static int createNotifyId(String convId) {
        if (!mNotifyId.containsKey(convId)) {
            for (int i = 0; i < 1000; i++) {
                if (!mNotifyId.containsValue(i)) {
                    mNotifyId.put(convId, i);
                    return i;
                }
            }
        }else {
            return (int)mNotifyId.get(convId);
        }
        return -1;
    }

    public static void closeNotification(String convId){
        //关闭通知
        int notifyId = getNotifyId(convId);
        if(notifyId != -1){
            mNotificationManager.cancel(notifyId);
            mNotifyId.remove(convId);
        }

    }

    public static int getNotifyId(String convId){
        if(mNotifyId.get(convId) == null){
            return -1;
        }else {
            return (int)mNotifyId.get(convId);
        }

    }

    public static boolean isOpenConv(String convId){
        return mCurrentConvIds.contains(convId);
    }

    public static void addCurrentConv(String convId){
        if(!mCurrentConvIds.contains(convId)){
            mCurrentConvIds.add(convId);
        }
    }

    public static void removeCurrenConv(String convId){
        mCurrentConvIds.remove(convId);
    }

}
