package com.example.weather.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.weather.MainActivity;
import com.example.weather.R;
import com.example.weather.adapter.AdapterFutureWeather;
import com.example.weather.adapter.AdapterHourWeather;
import com.example.weather.api.WeatherApi;
import com.example.weather.app_widget.MyWeatherWidget;
import com.example.weather.data.WeatherData;
import com.example.weather.model.CityModel;
import com.example.weather.model.WeatherFutureModel;
import com.example.weather.network.APICallback;
import com.example.weather.utils.DateTimeUtils;
import com.example.weather.utils.WeatherImg;
import com.intentfilter.androidpermissions.PermissionManager;
import com.intentfilter.androidpermissions.models.DeniedPermissions;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class WeatherService extends Service {
    public static final String TAG = MyWeatherWidget.TAG;
    //用於識別是來自於更新按鈕點擊事件的標籤
    public static final String UPDATE_EVENT = "onUpdate";
    private CityModel cityModel;
    private WeatherFutureModel weatherFutureModel;
    //時間的handler的唯一值tag
    private final int dateHandlerFlag = 1;
    //抓天氣api的時間間隔(ms)
    private final int weatherTimeInterval = 600000;
    //抓天氣api的定時器的tag
    private final String ALARM_ACTION = "weather_alarm";
    //抓天氣api的定時器
    private AlarmManager weatherAlarmManager;
    private PendingIntent weatherPendingIntent;

    /**天氣api的廣播器*/
    private final BroadcastReceiver weatherReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //write your code here
            //檢查權限同時呼叫天氣api
            checkPermission();

            //若版本高於6.0，需要重新設定定時器
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                weatherAlarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + weatherTimeInterval, weatherPendingIntent);
            }else{
                weatherAlarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                        SystemClock.elapsedRealtime() + weatherTimeInterval, weatherPendingIntent);
            }
        }
    };

    /**時間的handler*/
    @SuppressLint("HandlerLeak")
    private final Handler dateHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == dateHandlerFlag){
                update();
            }
        }
    };

    /**時間的run*/
    Runnable dateRunnable = new Runnable() {
        @Override
        public void run() {
            //設置flag
            dateHandler.sendEmptyMessage(dateHandlerFlag);
            //每秒更新一次
            dateHandler.postDelayed(this,1000);
        }
    };

    /**更新時間*/
    private void update(){
        Date date = DateTimeUtils.getNowTime();
        String time = DateTimeUtils.convertDistinctFormat("yyyy-MM-dd HH:mm:ss", date);
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
        cityModel = new CityModel(getApplicationContext());
        weatherFutureModel = new WeatherFutureModel();
        Log.d(TAG, "onCreate:(Service) ");

        dateHandler.sendEmptyMessage(dateHandlerFlag);
        dateHandler.post(dateRunnable);

        //設置抓天氣api的定時器
        IntentFilter intentFilter = new IntentFilter(ALARM_ACTION);
        registerReceiver(weatherReceive, intentFilter);

        //綁定定時器執行的動作
        weatherAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        weatherPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ALARM_ACTION), 0);

        //若版本高於6.0，在省電模式時使用此發法才可以準時觸發定時任務
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            weatherAlarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(), weatherPendingIntent);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            //若版本高於4.4，使用此發法可以比較準時
            weatherAlarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(), weatherPendingIntent);
        }else{
            weatherAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + weatherTimeInterval, weatherTimeInterval, weatherPendingIntent);
        }
    }

    /**當服務被kill時*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(weatherReceive);
        weatherAlarmManager.cancel(weatherPendingIntent);
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
                checkPermission();
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

    /**檢查權限*/
    private void checkPermission(){
        Set<String> permissionSet = new HashSet<>();
        permissionSet.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissionSet.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        PermissionManager permissionManager = PermissionManager.getInstance(getApplicationContext());
        permissionManager.checkPermissions(permissionSet, new PermissionManager.PermissionRequestListener() {
            @Override
            public void onPermissionGranted() {
                //取得定位權限成功
                cityModel.setLocationByGPS();

                //呼叫中央氣象局的api
                WeatherApi weatherApi = new WeatherApi(getApplicationContext());
                weatherApi.callWeatherApi(weatherCallback, cityModel.getFutureCodeByCounty(cityModel.getMyCountry()));
            }

            @Override
            public void onPermissionDenied(DeniedPermissions deniedPermissions) {
                //取得定位權限失敗
                //手動設定區域
                cityModel.setLocalRegion();
                //呼叫中央氣象局的api
                WeatherApi weatherApi = new WeatherApi(getApplicationContext());
                weatherApi.callWeatherApi(weatherCallback, cityModel.getFutureCodeByCounty(cityModel.getMyCountry()));
            }
        });
    }

    /**取得天氣api的回傳值*/
    private final APICallback<WeatherData> weatherCallback = new APICallback<WeatherData>() {

        @Override
        public void onFailure(int errorCode, String errorMessage) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "獲取天氣資訊失敗", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(WeatherData data) {
            //設定該地區的天氣資訊
            weatherFutureModel.setLocation(data.getWeatherRecord().getLocations().get(0).getLocation());
            weatherFutureModel.setWeatherElements(cityModel.getMyCity());
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    //取得當日溫度
                    //取得天氣頁面的元件
                    RemoteViews views = new RemoteViews(getPackageName(), R.layout.my_weather_widget);
                    views.setTextViewText(R.id.txt_town, cityModel.getMyCountry()+cityModel.getMyCity());
                    views.setImageViewResource(R.id.img_current_weather,
                            WeatherImg.getImgByWeather(Integer.parseInt(weatherFutureModel.getNowPhenomenon()[1])));
                    views.setTextViewText(R.id.txt_current_desc, weatherFutureModel.getNowPhenomenon()[0]);
                    views.setTextViewText(R.id.txt_current_temp, weatherFutureModel.getNowTemp() + "°C");
                    views.setTextViewText(R.id.txt_current_rain, weatherFutureModel.getNowRainProb() + "%");
                    views.setTextViewText(R.id.txt_current_detail, weatherFutureModel.getNowDesc());

                    //將天氣的類別與天氣頁面做綁定更新
                    AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
                    ComponentName componentName = new ComponentName(getApplicationContext(), MyWeatherWidget.class);
                    //通知AppWidget的onUpdate
                    manager.updateAppWidget(componentName, views);
                }
            });
        }
    };
}
