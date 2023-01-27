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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
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
import com.example.weather.data.WeatherEventData;
import com.example.weather.model.CityModel;
import com.example.weather.model.WeatherFutureModel;
import com.example.weather.network.APICallback;
import com.example.weather.utils.BitmapUtils;
import com.example.weather.utils.Constants;
import com.example.weather.utils.DateTimeUtils;
import com.example.weather.utils.Route;
import com.example.weather.utils.SharedPrefUtils;
import com.example.weather.utils.WeatherImg;
import com.gyf.immersionbar.ImmersionBar;
import com.intentfilter.androidpermissions.PermissionManager;
import com.intentfilter.androidpermissions.models.DeniedPermissions;

import org.greenrobot.eventbus.EventBus;

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
    private final int weatherTimeInterval = 3600000;
    //抓天氣api的定時器的tag
    private final String ALARM_ACTION = "weather_alarm";
    //抓天氣api的定時器
    private AlarmManager weatherAlarmManager;
    private PendingIntent weatherPendingIntent;
    //紀錄天氣廣播是否被註冊過
    private boolean isRegisterWeatherBro = false;

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

    /**螢幕喚醒狀態的廣播器*/
    private final BroadcastReceiver screenReceive = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
                //收到螢幕解鎖的通知
                startWeatherAlarm();
                Log.d(TAG, "螢幕解鎖");
            }else{
                //收到螢幕關閉的通知
                cancelWeatherAlarm();
                Log.d(TAG, "螢幕關閉");
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
        updateAppWidget(views);
    }

    /**可以透過onBind呼叫服務裡面的method*/
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        //參考:https://blog.csdn.net/shanshan_1117/article/details/80239932
        Log.d(TAG, "onBind:(Service) ");
        return new WeatherBinderService();
    }

    /**生命週期 : onCreate() ➞ onStartCommand() ➞ onDestroy()
     * 當服務被啟動時*/
    @Override
    public void onCreate() {
        super.onCreate();
        cityModel = new CityModel(getApplicationContext());
        weatherFutureModel = new WeatherFutureModel();
        Log.d(TAG, "onCreate:(Service) ");
        //啟用定時
        dateHandler.sendEmptyMessage(dateHandlerFlag);
        dateHandler.post(dateRunnable);

        //檢查權限同時呼叫天氣api
        checkPermission();
    }

    /**當服務被kill時*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenReceive);
        cancelWeatherAlarm();
    }

    /**當服務被開啟時*/
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStart:(Service) " + intent.getAction());
        Log.d(TAG, "onStart:(Service) " + intent.getCategories());
        if (!intent.hasCategory(Constants.MainAppCreate)){
            //倘若不是由主程式進入

            //偵測螢幕是否喚醒
            IntentFilter filter = new IntentFilter(Intent.ACTION_USER_PRESENT);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(screenReceive, filter);

            //啟用抓天氣api
            startWeatherAlarm();
        }
        //監聽點擊事件
        if (intent.getAction() != null){
            //每次點擊按鈕時，intent就會送一個廣播出來
            if (intent.getAction().equals(UPDATE_EVENT)){
                //更新
                Log.d(TAG, "Click method ");
                checkPermission();

                //作動畫
                Bitmap bitmap = BitmapFactory.decodeResource(
                        getApplicationContext().getResources(), R.drawable.refresh);
                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.my_weather_widget);
                for (int i = 0; i < 37; i++) {
                    remoteViews.setImageViewBitmap(R.id.img_update,
                            BitmapUtils.rotateBitmap(bitmap, (i * 10) % 360));

                    updateAppWidget(remoteViews);
                }
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
        Intent appIntent = new Intent(getApplicationContext(), MainActivity.class);
        //設定背景點擊事件，使用PendingIntent開啟首頁
        PendingIntent appPendingIntent = PendingIntent.getActivity(this, 0, appIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.lin_main, appPendingIntent);
        manager.updateAppWidget(thisWidget, remoteViews);
    }

    /**啟用抓天氣api的定時器以及廣播*/
    private void startWeatherAlarm(){
        //註冊抓天氣api的廣播接收器
        IntentFilter intentFilter = new IntentFilter(ALARM_ACTION);
        registerReceiver(weatherReceive, intentFilter);
        isRegisterWeatherBro = true;

        //設置抓天氣api的定時器
        //綁定定時器執行的動作
        weatherAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        weatherPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ALARM_ACTION), 0);
        if(weatherPendingIntent != null){
            weatherAlarmManager.cancel(weatherPendingIntent);
        }

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

    /**取消抓天氣api的定時器以及廣播*/
    private void cancelWeatherAlarm(){
        if(isRegisterWeatherBro){
            unregisterReceiver(weatherReceive);
            weatherAlarmManager.cancel(weatherPendingIntent);
            isRegisterWeatherBro = false;
        }
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
            //透過eventbus傳遞資料
            EventBus mEventBus = EventBus.getDefault();;
            WeatherEventData weatherEventData = new WeatherEventData();
            weatherEventData.setCode(0);
            weatherEventData.setErrorData(errorMessage);
            mEventBus.post(weatherEventData);
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(), "獲取天氣資訊失敗", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(WeatherData data) {
            //透過eventbus傳遞資料
            EventBus mEventBus = EventBus.getDefault();;
            WeatherEventData weatherEventData = new WeatherEventData();
            weatherEventData.setCode(1);
            weatherEventData.setWeatherData(data);
            mEventBus.post(weatherEventData);
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
                    views.setInt(R.id.lin_main, "setBackgroundResource", getCurrentMode());

                    updateAppWidget(views);
                }
            });
        }
    };

    /**將某個頁面與桌面小元件做綁定更新*/
    private void updateAppWidget(RemoteViews views){
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName componentName = new ComponentName(getApplicationContext(), MyWeatherWidget.class);
        //通知AppWidget的onUpdate
        manager.updateAppWidget(componentName, views);
    }

    /**根據當前模式，選擇佈景主題*/
    private int getCurrentMode(){
        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(getApplicationContext());
        int bgColor;
        if(sharedPrefUtils.getLastMode() == 0)
            bgColor = R.drawable.gradient_light_background;
        else
            bgColor = R.drawable.gradient_night_background;

        return bgColor;
    }

    /**實作天氣api的服務綁定的類別*/
    private class WeatherBinderService extends Binder implements WeatherBinder {
        @Override
        public void getWeatherData() {
            checkPermission();
        }
    }
}
