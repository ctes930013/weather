package com.example.weather.app_widget;

import android.app.ActivityManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.weather.R;
import com.example.weather.service.WeatherService;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class MyWeatherWidget extends AppWidgetProvider {
    public static final String TAG = "Weather";

    /**當接收到更新事件ACTION_APPWIDGET_UPDATE時*/
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        Boolean isRun = isServiceRun(context);
        Log.d(TAG, "onUpdate: 有Service再跑？: " + isRun);
        if (!isRun) startRunService(context);
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
        startRunService(context);
    }

    /**當小工具被刪除時*/
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(TAG, "桌面小工具被移除囉");
        context.stopService(new Intent(context, WeatherService.class));
    }

    /**啟動Service*/
    private void startRunService(Context context) {
        Intent intent = new Intent(context, WeatherService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android 8.0後必須加入以下，否則服務會很快被背景殺死
            context.startForegroundService(intent);
        }
        context.startService(intent);
    }

    /**判斷此是否已有我們的Service再跑，避免無限重啟服務*/
    private Boolean isServiceRun(Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list =  manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : list){
            //檢查有無找到我們的服務
            if (WeatherService.class.getName().equals(info.service.getClassName()))return true;
        }
        return false;
    }
}