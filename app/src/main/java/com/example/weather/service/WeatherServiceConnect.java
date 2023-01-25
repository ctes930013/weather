package com.example.weather.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 綁定的天氣類別
 *
 */
public class WeatherServiceConnect implements ServiceConnection {

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        //先轉型為WeatherBinder同時呼叫取得天氣資訊api
        WeatherBinder weatherBinder = (WeatherBinder) iBinder;
        weatherBinder.getWeatherData();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }
}
