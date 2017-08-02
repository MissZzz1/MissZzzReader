package com.zhao.myreader.entity;

import java.io.Serializable;

/**
 * Created by zhao on 2016/10/25.
 */



public class JsonModel implements Serializable {
    private int error;//错误码
    private boolean success;//请求是否成功
    private String result;//服务器返回的json数据存放与此
    private String token;
    private int datasize;
    private String publicKey;


    private int visibleLastIndex = 0;
    private int visibleItemCount;


    public JsonModel() {

    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public int getError() {
        return this.error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getVisibleLastIndex() {
        return this.visibleLastIndex;
    }

    public void setVisibleLastIndex(int visibleLastIndex) {
        this.visibleLastIndex = visibleLastIndex;
    }

    public int getVisibleItemCount() {
        return this.visibleItemCount;
    }

    public void setVisibleItemCount(int visibleItemCount) {
        this.visibleItemCount = visibleItemCount;
    }

    public int getDatasize() {
        return this.datasize;
    }

    public void setDatasize(int datasize) {
        this.datasize = datasize;
    }


    @Override
    public String toString() {
        return "JsonModel{" +
                "error=" + error +
                ", success=" + success +
                ", result='" + result + '\'' +
                ", token='" + token + '\'' +
                ", datasize=" + datasize +
                ", visibleLastIndex=" + visibleLastIndex +
                ", visibleItemCount=" + visibleItemCount +
                '}';
    }
}
