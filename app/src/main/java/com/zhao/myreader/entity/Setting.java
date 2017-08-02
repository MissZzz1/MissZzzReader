package com.zhao.myreader.entity;

import com.zhao.myreader.enums.ReadStyle;

import java.io.Serializable;

/**
 * 用户设置
 * Created by zhao on 2017/7/28.
 */

public class Setting implements Serializable {

    private int readWordColor;//阅读字体颜色
    private int readBgColor;//阅读背景颜色
    private float readWordSize;//阅读字体大小

    private ReadStyle readStyle;//阅读模式

    private boolean dayStyle;//是否日间模式

    public boolean isDayStyle() {
        return dayStyle;
    }

    public void setDayStyle(boolean dayStyle) {
        this.dayStyle = dayStyle;
    }

    public int getReadWordColor() {
        return readWordColor;
    }

    public void setReadWordColor(int readWordColor) {
        this.readWordColor = readWordColor;
    }

    public int getReadBgColor() {
        return readBgColor;
    }

    public void setReadBgColor(int readBgColor) {
        this.readBgColor = readBgColor;
    }

    public float getReadWordSize() {
        return readWordSize;
    }

    public void setReadWordSize(float readWordSize) {
        this.readWordSize = readWordSize;
    }

    public ReadStyle getReadStyle() {
        return readStyle;
    }

    public void setReadStyle(ReadStyle readStyle) {
        this.readStyle = readStyle;
    }
}
