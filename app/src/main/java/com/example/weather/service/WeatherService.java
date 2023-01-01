package com.example.weather.service;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weather.R;
import com.example.weather.app_widget.MyWeatherWidget;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class WeatherService extends Service implements Runnable {
    public static final String TAG = MyWeatherWidget.TAG;
    //用於識別是來自於更新按鈕點擊事件的標籤
    public static final String UPDATE_EVENT = "onUpdate";
    @SuppressLint("SimpleDateFormat")
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss", Locale.TAIWAN);

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1){
                update();
            }
        }
    };

    @Override
    public void run() {
        //設置flag
        handler.sendEmptyMessage(1);
        //每秒更新一次
        handler.postDelayed(this,1000);
    }

    /**更新時間*/
    private void update(){
        Calendar c = Calendar.getInstance();
        //設定東八區
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        String time = sdf.format(c.getTime());
        //取得天氣頁面的元件
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.my_weather_widget);
        views.setTextViewText(R.id.txt_time, time);
        //將天氣的類別與天氣頁面做綁定更新
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName = new ComponentName(getApplicationContext(), MyWeatherWidget.class);
        //通知AppWidget的onUpdate
        manager.updateAppWidget(componentName, views);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**生命週期 : onCreate() ➞ onStartCommand() ➞ onDestroy()
     * 當服務被啟動時*/
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate:(Service) ");
        handler.sendEmptyMessage(1);
        handler.post(this);
    }

    /**當服務被開啟時*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStart:(Service) " + intent.getAction());
        //監聽點擊事件
        if (intent.getAction() != null){
            //每次點擊按鈕時，intent就會送一個廣播出來
            if (intent.getAction().equals(UPDATE_EVENT)){
                Log.d(TAG, "Click method ");
                Handler toastHandler = new Handler(Looper.getMainLooper());
                toastHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "點擊了小物件", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        setOnUpdateClick();
        return START_STICKY;
    }

    /**設置更新按鈕廣播發送事件*/
    private void setOnUpdateClick() {
        ComponentName thisWidget = new ComponentName(this, MyWeatherWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.my_weather_widget);
        Intent myIntent = new Intent();
        myIntent.setAction(UPDATE_EVENT);

        //設定按鈕點擊事件，使用PendingIntent作為發送intent
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.img_update, pendingIntent);
        manager.updateAppWidget(thisWidget, remoteViews);
    }
}
