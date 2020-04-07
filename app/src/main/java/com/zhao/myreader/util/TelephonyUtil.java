package com.zhao.myreader.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;

import java.lang.reflect.Method;

/**
 * Created by zhao on 2017/6/27.
 */

public class TelephonyUtil {


    public static String getNum1(Context context) {
        String tel = "";
        String IMSI;
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                tel = tm.getLine1Number();//手机号码
                Class clazz = tm.getClass();
                Method getPhoneNumber = clazz.getDeclaredMethod("getLine1NumberForSubscriber", int.class);
                String tel0 = (String) getPhoneNumber.invoke(tm, 0);
                String tel1 = (String) getPhoneNumber.invoke(tm, 1);

                IMSI = tm.getSubscriberId();
                TextHelper.showText(IMSI);
            }

        } catch (Exception e) {
            e.printStackTrace();
            tel = "";
        }


        return tel;
    }

    public static String getNum2(Context context) {
        String tel = "";
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                tel = tm.getLine1Number();//手机号码
            }


        }catch (Exception e){
            e.printStackTrace();
            tel = "";
        }

        return tel;
    }

}
