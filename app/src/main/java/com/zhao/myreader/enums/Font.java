package com.zhao.myreader.enums;

/**
 * Created by zhao on 2016/11/3.
 */

public enum Font {


    默认字体(""),
    方正楷体("fonts/fangzhengkaiti.ttf"),
    方正行楷("fonts/fangzhengxingkai.ttf"),
    经典宋体("fonts/songti.ttf"),
    迷你隶书("fonts/mini_lishu.ttf"),
    方正黄草("fonts/fangzhenghuangcao.ttf"),
    书体安景臣钢笔行书("fonts/shuti_anjingchen_gangbixingshu.ttf");


    public String path;

    Font(String path) {
        this.path = path;
    }

    public static Font get(int var0) {
        return values()[var0];
    }

    public static Font fromString(String string) {
        return Font.valueOf(string);
    }
}
