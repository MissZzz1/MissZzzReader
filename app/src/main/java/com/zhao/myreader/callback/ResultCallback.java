package com.zhao.myreader.callback;


/**
 * Created by zhao on 2016/10/25.
 */

public interface ResultCallback {

    void onFinish(Object o, int code);

    void onError(Exception e);

}
