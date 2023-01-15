package com.example.weather.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.weather.MainActivity;
import com.example.weather.page.SetCity;
import com.example.weather.page.SetCounty;
import com.example.weather.page.Settings;

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

    /**
     * 跳轉設定頁面
     *
     */
    public void toSettings() {
        Intent intent = new Intent();
        intent.setClass(context, Settings.class);
        context.startActivity(intent);
    }

    /**
     * 跳轉設定縣市頁面
     *
     */
    public void toSetCounty() {
        Intent intent = new Intent();
        intent.setClass(context, SetCounty.class);
        context.startActivity(intent);
    }

    /**
     * 跳轉設定鄉鎮市區頁面同時傳入縣市
     *
     * @param county  縣市
     */
    public void toSetCity(String county) {
        Intent intent = new Intent();
        intent.setClass(context, SetCity.class);
        Bundle bundle = new Bundle();
        bundle.putString("county", county);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
