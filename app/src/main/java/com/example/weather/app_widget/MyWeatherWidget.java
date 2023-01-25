package com.example.weather.app_widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.weather.service.WeatherService;
import com.example.weather.utils.ServiceUtils;

/**
 * Implementation of App Widget functionality.
 */
public class MyWeatherWidget extends AppWidgetProvider {
    public static final String TAG = "Weather";

    /**當接收到更新事件ACTION_APPWIDGET_UPDATE時*/
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Boolean isRun = ServiceUtils.isServiceRun(context, WeatherService.class.getName());
        Log.d(TAG, "onUpdate: Service狀態: " + isRun);
        if (!isRun) {
            //若當前服務沒有被啟用則啟用服務
            Intent intent = new Intent(context, WeatherService.class);
            ServiceUtils.startRunService(context, intent);
        }
    }

    /**接收廣播資訊*/
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d(TAG, "onReceive: "+intent.getAction());
    }

    /**當小工具被建立時*/
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        Log.d(TAG, "桌面小工具被建立囉");
        //啟用服務
        Intent intent = new Intent(context, WeatherService.class);
        ServiceUtils.startRunService(context, intent);
    }

    /**當小工具被刪除時*/
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(TAG, "桌面小工具被移除囉");
        context.stopService(new Intent(context, WeatherService.class));
    }
}