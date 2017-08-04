package com.zhao.myreader.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.provider.Settings;
import android.view.WindowManager;

/**
 * Created by zhao on 2017/8/4.
 */

public class BrightUtil {
    /**
     * 获取屏幕的亮度
     *
     * @param activity
     * @return
     */
    public static int getScreenBrightness(Activity activity) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = activity.getContentResolver();
        try {
            nowBrightnessValue = android.provider.Settings.System.getInt(
                    resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }
    /**
     * 设置亮度
     *
     * @param activity
     * @param brightness
     */
    public static void setBrightness(Activity activity, int brightness) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
        activity.getWindow().setAttributes(lp);
    }

    public static int brightToProgress(int brightness){
        return (int)(Float.valueOf(brightness) * (1f / 255f) * 100);
    }

    public static int progressToBright(int progress){
        return progress  * 255 / 100;
    }

    /**
     * 亮度跟随系统
     * @param activity
     */
    public static void followSystemBright(Activity activity){
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.screenBrightness =   WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
        activity.getWindow().setAttributes(lp);
    }
}
