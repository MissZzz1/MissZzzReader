package com.zhao.myreader.util;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by zhao on 2017/4/7.
 */

public class JsonArrayToObjectArray {

    public static <T> ArrayList<T> getArray(String json, Class<T> c) throws Exception {
        ArrayList<T> arrayList = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            arrayList.add(new Gson().fromJson(jsonArray.getString(i), c));
        }

        return arrayList;
    }

}
