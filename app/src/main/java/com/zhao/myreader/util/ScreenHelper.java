package com.zhao.myreader.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by zhao on 2017/3/10.
 */

public class ScreenHelper {

    public static double getScreenPhysicalSize(Activity ctx) {
        DisplayMetrics dm = new DisplayMetrics();
        ctx.getWindowManager().getDefaultDisplay().getMetrics(dm);
        double diagonalPixels = Math.sqrt(Math.pow(dm.widthPixels, 2) + Math.pow(dm.heightPixels, 2));
        return diagonalPixels / (160 * dm.density);
    }
}
