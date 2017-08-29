package com.zhao.myreader.entity;

import java.io.Serializable;

/**
 * Created by zhao on 2017/8/2.
 */

public class UpdateInfo implements Serializable{

    private static final long serialVersionUID = 8136214121542689902L;

    private int newestVersionCode;
    private String newestVersionName;
    private String downLoadUrl;

    public int getNewestVersionCode() {
        return newestVersionCode;
    }

    public void setNewestVersionCode(int newestVersionCode) {
        this.newestVersionCode = newestVersionCode;
    }

    public String getNewestVersionName() {
        return newestVersionName;
    }

    public void setNewestVersionName(String newestVersionName) {
        this.newestVersionName = newestVersionName;
    }

    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }
}
