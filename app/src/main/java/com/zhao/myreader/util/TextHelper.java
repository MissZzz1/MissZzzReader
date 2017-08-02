package com.zhao.myreader.util;

import android.widget.Toast;

import com.zhao.myreader.application.MyApplication;


/**
 * Created by zhao on 2016/10/26.
 */

public class TextHelper {

    public static void showText(final String text){

        MyApplication.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyApplication.getApplication(),text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void showLongText(final String text){

        MyApplication.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyApplication.getApplication(),text, Toast.LENGTH_LONG).show();
            }
        });
    }
}