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

    public static final String FILE_DIR = "MissZzzReader";
    public static final String TEM_FILE_DIR = Environment.getExternalStorageDirectory() + "/MissZzzReader/tem/";
    public static final String UPDATE_APK_FILE_DIR = "MissZzzReader/apk/";
    public static long exitTime;
    public static final int exitConfirmTime = 2000;

    public static final String BOOK = "book";


    public static final int[] READ_STYLE_NIGHT = {R.color.sys_night_word, R.color.sys_night_bg};//黑夜
    public static final int[] READ_STYLE_PROTECTED_EYE = {R.color.sys_protect_eye_word, R.color.sys_protect_eye_bg};//护眼
    public static final int[] READ_STYLE_COMMON = {R.color.sys_common_word, R.color.sys_common_bg};//普通
    public static final int[] READ_STYLE_BLUE_DEEP = {R.color.sys_blue_deep_word, R.color.sys_blue_deep_bg};//深蓝
    public static final int[] READ_STYLE_LEATHER = {R.color.sys_leather_word, R.mipmap.theme_leather_bg};//羊皮纸
    public static final int[] READ_STYLE_BREEN_EYE = {R.color.sys_breen_word, R.color.sys_breen_bg};//棕绿色


    public static final String FILE_NAME_SETTING = "setting";
    public static final String FILE_NAME_UPDATE_INFO = "updateInfo";



}
