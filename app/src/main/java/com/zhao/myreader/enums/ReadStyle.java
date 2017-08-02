package com.zhao.myreader.enums;

/**
 * Created by zhao on 2016/11/3.
 */

public enum ReadStyle {

    protectedEye,//现场取件
    night;//网络取件

    ReadStyle() {


    }

    public static ReadStyle get(int var0) {
        return values()[var0];
    }

    public static ReadStyle fromString(String string) {
        return ReadStyle.valueOf(string);
    }
}
