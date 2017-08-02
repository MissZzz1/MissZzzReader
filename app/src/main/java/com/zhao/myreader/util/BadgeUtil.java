package com.zhao.myreader.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by zhao on 2017/5/22.
 */

public class BadgeUtil {

    /**
     * Set badge count<br/>
     * 针对 Samsung / xiaomi / sony 手机有效
     * @param context The context of the application package.
     * @param count Badge count to be set
     */
    @TargetApi(16)
    public static void setBadgeCount(Context context, int count) {
        if (count <= 0) {
            count = 0;
        } else {
            count = Math.max(0, Math.min(count, 99));
        }

        if (Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")) {
            NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Notification.Builder builder = new Notification.Builder(context).setContentTitle("title").setContentText("text");

            Notification notification = builder.build();

            try {

                Field field = notification.getClass().getDeclaredField("extraNotification");

                Object extraNotification = field.get(notification);

                Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);

                method.invoke(extraNotification, count);

                mNotificationManager.notify(0,notification);
                sendToXiaoMi(notification, context, count);

            } catch (Exception e) {

                e.printStackTrace();

            }


        } else if (Build.MANUFACTURER.equalsIgnoreCase("sony")) {
            sendToSony(context, count);
        } else if (Build.MANUFACTURER.toLowerCase().contains("samsung")) {
            sendToSamsumg(context, count);
        } else {
//            Toast.makeText(context, "Not Support", Toast.LENGTH_LONG).show();
        }
    }




    /**
     * 向小米手机发送未读消息数广播
     * @param count
     */
    private static void sendToXiaoMi(Notification notification, Context context, int count) {
        try {
//            Class miuiNotificationClass = Class.forName("android.app.MiuiNotification");
//            Object miuiNotification = miuiNotificationClass.newInstance();
//            Field field = miuiNotification.getClass().getDeclaredField("messageCount");
//            field.setAccessible(true);
//            field.set(miuiNotification, String.valueOf(count == 0 ? "" : count));  // 设置信息数-->这种发送必须是miui 6才行

            Field field = notification.getClass().getDeclaredField("extraNotification");

            Object extraNotification = field.get(notification);

            Method method = extraNotification.getClass().getDeclaredMethod("setMessageCount", int.class);

            method.invoke(extraNotification, count);

        } catch (Exception e) {
            e.printStackTrace();
            // miui 6之前的版本
            Intent localIntent = new Intent(
                    "android.intent.action.APPLICATION_MESSAGE_UPDATE");
            localIntent.putExtra(
                    "android.intent.extra.update_application_component_name",
                    context.getPackageName() + "/" + getLauncherClassName(context));
            localIntent.putExtra(
                    "android.intent.extra.update_application_message_text", String.valueOf(count == 0 ? "" : count));
            context.sendBroadcast(localIntent);
        }
    }


    /**
     * 向索尼手机发送未读消息数广播<br/>
     * 据说：需添加权限：<uses-permission android:name="com.sonyericsson.home.permission.BROADCAST_BADGE" /> [未验证]
     * @param count
     */
    private static void sendToSony(Context context, int count){
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }

        boolean isShow = true;
        if (count == 0) {
            isShow = false;
        }
        Intent localIntent = new Intent();
        localIntent.setAction("com.sonyericsson.home.action.UPDATE_BADGE");
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.SHOW_MESSAGE",isShow);//是否显示
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.ACTIVITY_NAME",launcherClassName );//启动页
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.MESSAGE", String.valueOf(count));//数字
        localIntent.putExtra("com.sonyericsson.home.intent.extra.badge.PACKAGE_NAME", context.getPackageName());//包名
        context.sendBroadcast(localIntent);
    }


    /**
     * 向三星手机发送未读消息数广播
     * @param count
     */
    private static void sendToSamsumg(Context context, int count){
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }


    /**
     * 重置、清除Badge未读显示数<br/>
     * @param context
     */
  /*  public static void resetBadgeCount(Notification notification,Context context) {
        setBadgeCount(notification, context, 0);
    }*/


    /**
     * Retrieve launcher activity name of the application from the context
     *
     * @param context The context of the application package.
     * @return launcher activity name of this application. From the
     *         "android:name" attribute.
     */
    private static String getLauncherClassName(Context context) {
        PackageManager packageManager = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        // To limit the components this Intent will resolve to, by setting an
        // explicit package name.
        intent.setPackage(context.getPackageName());
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        // All Application must have 1 Activity at least.
        // Launcher activity must be found!
        ResolveInfo info = packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);

        // get a ResolveInfo containing ACTION_MAIN, CATEGORY_LAUNCHER
        // if there is no Activity which has filtered by CATEGORY_DEFAULT
        if (info == null) {
            info = packageManager.resolveActivity(intent, 0);
        }

        return info.activityInfo.name;
    }
}
