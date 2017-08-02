package com.zhao.myreader.callback;


import com.zhao.myreader.entity.JsonModel;

/**
 * Created by zhao on 2016/10/25.
 */

public interface JsonCallback {

    void onFinish(JsonModel jsonModel);

    void onError(Exception e);

}
