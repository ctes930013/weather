package com.example.weather.utils;

import android.content.Context;
import android.content.Intent;

import com.example.weather.MainActivity;

/**
 * 路由頁面跳轉處理相關
 *
 */
public class Route {

    Context context;
    public Route(Context context){
        this.context = context;
    }

    /**
     * 跳轉首頁並清除之前所有頁面
     *
     */
    public void toIndex() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, MainActivity.class);
        context.startActivity(intent);
    }
}
