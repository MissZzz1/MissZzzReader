package com.zhao.myreader.common;

import android.os.Environment;

import com.zhao.myreader.R;


/**
 * Created by zhao on 2016/10/20.
 */

public class APPCONST {



    public static String publicKey;//服务端公钥
    public static String privateKey;//app私钥
    public final static String s = "11940364935628058505";



    public static final String ALARM_SCHEDULE_MSG = "alarm_schedule_msg";

    public static final String FILE_DIR = "gxdw";
    public static final String TEM_FILE_DIR = Environment.getExternalStorageDirectory() + "/gxdw/tem/";
    public static long exitTime;
    public static final int exitConfirmTime = 2000;

    public static final String BOOK = "book";



    public static final int[] READ_STYLE_PROTECTED_EYE = {R.color.sys_protect_eye_word, R.color.sys_protect_eye_bg};
    public static final int[] READ_STYLE_NIGHT = {R.color.sys_night_work, R.color.sys_night_bg};

    public static final String FILE_NAME_SETTING = "setting";


}
