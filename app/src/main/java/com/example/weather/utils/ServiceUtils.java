package com.example.weather.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;

import com.example.weather.service.WeatherService;

import java.util.List;

/**
 * 服務處理相關
 *
 */
public class ServiceUtils {

    /**
     * 啟動某個Service
     *
     * @param intent  Service的intent
     *
     */
    public static void startRunService(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Android 8.0後必須加入以下，否則服務會很快被背景殺死
            context.startForegroundService(intent);
        }
        context.startService(intent);
    }

    /**
     * 停用某個Service
     *
     * @param intent  Service的intent
     *
     */
    public static void stopService(Context context, Intent intent) {
        context.stopService(intent);
    }

    /**
     * 與某個Service作綁定
     *
     * @param intent  Service的intent
     * @param c  欲綁定的類別(需實作ServiceConnection)
     *
     */
    public static void bindService(Context context, Intent intent, ServiceConnection c) {
        context.bindService(intent, c, Context.BIND_AUTO_CREATE);
    }

    /**
     * 判斷此是否已有我們的Service再跑，避免無限重啟服務
     *
     * @param serviceName  Service的名稱
     *
     */
    public static Boolean isServiceRun(Context context, String serviceName){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list =  manager.getRunningServices(Integer.MAX_VALUE);
        for (ActivityManager.RunningServiceInfo info : list){
            //檢查有無找到我們的服務
            if (serviceName.equals(info.service.getClassName())) return true;
        }
        return false;
    }
}
