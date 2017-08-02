package com.zhao.myreader.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by zhao on 2017/3/2.
 */

public class PermissionHelper {

    /**
     * 设备信息读取权限
     * @param context
     * @return
     */
    public static boolean isREAD_PHONE_STATE(Context context) {
        boolean permission = false;
        if (Build.VERSION.SDK_INT >= 23) {
            int checkReadPhoneStatePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
            if (checkReadPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 1);
                TextHelper.showText("当前应用未拥读取设备状态权限");
            } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_PHONE_STATE}, 1);
                TextHelper.showText("当前应用未拥读取设备状态权限");
            } else {
                permission = true;
            }
        } else {
            permission = true;
        }
        return permission;
    }

    /**
     * 蓝牙设备权限
     * @param context
     * @return
     */
    public static boolean isACCESS_COARSE_LOCATION(Context context) {
        boolean permission = false;
        if (Build.VERSION.SDK_INT >= 23) {
            int checkReadPhoneStatePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (checkReadPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                TextHelper.showText("当前应用未拥有蓝牙设备使用权限");
            } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                TextHelper.showText("当前应用未拥有蓝牙设备使用权限");
            } else {
                permission = true;
            }
        } else {
            permission = true;
        }
        return permission;
    }


    /**
     * 文件读写权限
     * @param context
     * @return
     */
    public static boolean isWRPermission(Context context) {
        boolean permission = false;
        if (Build.VERSION.SDK_INT >= 23) {
            int checkReadPhoneStatePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (checkReadPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                TextHelper.showText("当前应用未拥有存储设备读写权限");
            } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                TextHelper.showText("当前应用未拥有存储设备读写权限");
            } else {
                permission = true;
            }
        } else {
            permission = true;
        }
        return permission;
    }

    /**
     * 声音设备权限
     * @param context
     * @return
     */
    public static boolean isAudioPermission(Context context) {
        boolean permission = false;
        if (Build.VERSION.SDK_INT >= 23) {
            int checkReadPhoneStatePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO);
            if (checkReadPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
                TextHelper.showText("当前应用未拥有音频录制权限");
            } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.RECORD_AUDIO}, 1);
                TextHelper.showText("当前应用未拥有音频录制权限");
            } else {
                permission = true;
            }
        } else {
            permission = true;
        }
        return permission;
    }

    /**
     * 摄像头权限
     * @param context
     * @return
     */
    public static boolean isCameraPermission(Context context) {
        boolean permission = false;
        if (Build.VERSION.SDK_INT >= 23) {
            int checkReadPhoneStatePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
            if (checkReadPhoneStatePermission != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA}, 1);
                TextHelper.showText("当前应用未拥有调用摄像头权限");
            } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                // 弹出对话框接收权限
                ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.CAMERA}, 1);
                TextHelper.showText("当前应用未拥有调用摄像头权限");
            } else {
                permission = true;
            }
        } else {
            permission = true;
        }
        return permission;
    }



}
