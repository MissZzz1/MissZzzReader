package com.zhao.myreader.callback;

import android.graphics.Bitmap;

import java.io.InputStream;

/**
 * Created by zhao on 2016/4/16.
 */
public interface HttpCallback {
    void onFinish(String response);
    void onFinish(InputStream in);
    void onFinish(Bitmap bm);
    void onError(Exception e);
}
