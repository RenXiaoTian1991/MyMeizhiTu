package com.example.lenovo.ganhuoapp.utils;

import com.google.gson.Gson;

/**
 * Created by lenovo on 2015/11/5.
 */
public class GsonUtils {

    public static <T> T json2bean(String json,Class<T> clazz){
        Gson gson = new Gson();
        T t = gson.fromJson(json,clazz);
        return t;
    }

}
