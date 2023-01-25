package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.adapter.AdapterFutureWeather;
import com.example.weather.adapter.AdapterHourWeather;
import com.example.weather.api.WeatherApi;
import com.example.weather.data.WeatherData;
import com.example.weather.data.WeatherEventData;
import com.example.weather.model.CityModel;
import com.example.weather.model.WeatherFutureModel;
import com.example.weather.network.APICallback;
import com.example.weather.page.Settings;
import com.example.weather.service.WeatherBinder;
import com.example.weather.service.WeatherService;
import com.example.weather.utils.Constants;
import com.example.weather.utils.DateTimeUtils;
import com.example.weather.utils.GeocoderMgr;
import com.example.weather.utils.Route;
import com.example.weather.utils.ServiceUtils;
import com.example.weather.utils.SharedPrefUtils;
import com.example.weather.utils.WeatherImg;
import com.gyf.immersionbar.ImmersionBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private SharedPrefUtils sharedPrefUtils;
    private CityModel cityModel;
    private WeatherFutureModel weatherFutureModel;
    private LinearLayout linMain;
    private ImageView imgCurrentWeather, imgSetting;
    private TextView txtTown, txtCurrentTemp, txtCurrentDesc, txtCurrentTempRange;
    private RecyclerView recyclerViewWeatherHour, recyclerViewWeather;
    private AdapterHourWeather adapterHourWeather;
    private AdapterFutureWeather adapterFutureWeather;
    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        linMain = findViewById(R.id.lin_main);
        imgCurrentWeather = findViewById(R.id.img_current_weather);
        imgSetting = findViewById(R.id.img_setting);
        txtTown = findViewById(R.id.txt_town);
        txtCurrentTemp = findViewById(R.id.txt_current_temp);
        txtCurrentDesc = findViewById(R.id.txt_current_desc);
        txtCurrentTempRange = findViewById(R.id.txt_current_temp_range);
        recyclerViewWeatherHour = findViewById(R.id.recycler_view_weather_hour);
        recyclerViewWeather = findViewById(R.id.recycler_view_weather);
        recyclerViewWeatherHour.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewWeather.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewWeather.setNestedScrollingEnabled(true);
        sharedPrefUtils = new SharedPrefUtils(this);

        //根據時段選擇背景顏色
        setCurrentMode();
        getCurrentMode();


        cityModel = new CityModel(this);
        weatherFutureModel = new WeatherFutureModel();

        //權限檢查
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

        //跳轉設定頁面
        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Route route = new Route(MainActivity.this);
                route.toSettings();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventBus.unregister(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //取得當前所在的經緯度
        getLocal();
    }

    //接收天氣api回傳回來的資料
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeatherEventData eventData) {
        if(eventData.getCode() == 1){
            //成功
            //設定該地區的天氣資訊
            WeatherData data = eventData.getWeatherData();
            weatherFutureModel.setLocation(data.getWeatherRecord().getLocations().get(0).getLocation());
            weatherFutureModel.setWeatherElements(cityModel.getMyCity());
            //取得當日溫度
            imgCurrentWeather.setImageResource(
                    WeatherImg.getImgByWeather(Integer.parseInt(weatherFutureModel.getNowPhenomenon()[1])));
            txtCurrentTemp.setText(weatherFutureModel.getNowTemp() + "°C");
            txtCurrentDesc.setText(weatherFutureModel.getNowPhenomenon()[0]);
            //取得當日體感溫度
            txtCurrentTempRange.setText(weatherFutureModel.getNowRealTemp() + "°C");

            //取得未來幾小時天氣預報
            adapterHourWeather = new AdapterHourWeather(weatherFutureModel);
            recyclerViewWeatherHour.setAdapter(adapterHourWeather);

            //取得未來幾天天氣預報
            adapterFutureWeather = new AdapterFutureWeather(weatherFutureModel);
            recyclerViewWeather.setAdapter(adapterFutureWeather);
        }else{
            //失敗
            Toast.makeText(MainActivity.this, "獲取天氣資訊失敗", Toast.LENGTH_SHORT).show();
        }
    }

    //取得當前所在位置
    private void getLocal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //沒有權限
                //手動設定區域
                cityModel.setLocalRegion();
            } else {
                //自動根據定位設定區域
                cityModel.setLocationByGPS();
            }
        } else {
            //自動根據定位設定區域
            cityModel.setLocationByGPS();
        }


        txtTown.setText(cityModel.getMyCountry()+cityModel.getMyCity());

        //呼叫中央氣象局的api
        //先檢查服務
        Boolean isRun = ServiceUtils.isServiceRun(MainActivity.this, WeatherService.class.getName());
        Intent intent = new Intent(MainActivity.this, WeatherService.class);
        //打一個暗號表示桌面小工具被建立
        intent.addCategory(Constants.MainAppCreate);
        if (!isRun) {
            //若當前服務沒有被啟用則啟用服務
            ServiceUtils.startRunService(MainActivity.this, intent);
        } else {
            //若當前服務有被啟用則直接綁定服務
            WeatherServiceConnect weatherServiceConnect = new WeatherServiceConnect();
            ServiceUtils.bindService(MainActivity.this, intent, weatherServiceConnect);
        }
    }

    //設定當前模式
    private void setCurrentMode(){
        int hours = Integer.parseInt(DateTimeUtils.convertDistinctFormat("HH", DateTimeUtils.getNowTime()));
        if (hours >= 6 && hours <= 18) {
            sharedPrefUtils.setLastMode(0);
        } else {
            sharedPrefUtils.setLastMode(1);
        }
    }

    //根據當前模式，選擇佈景主題
    private void getCurrentMode(){
        int bgColor;
        if(sharedPrefUtils.getLastMode() == 0)
            bgColor = R.color.light_bg;
        else
            bgColor = R.color.night_bg;

        //設定背景顏色
        linMain.setBackgroundColor(getResources().getColor(bgColor));
        //設定狀態欄顏色
        ImmersionBar.with(this)
                .statusBarDarkFont(sharedPrefUtils.getLastMode() == 0)
                .statusBarColor(bgColor)
                .fitsSystemWindows(true)   //避免畫面上方和通知欄重疊到
                .navigationBarDarkIcon(sharedPrefUtils.getLastMode() == 0)
                .navigationBarColor(bgColor)
                .init();
    }

    //綁定的天氣類別
    private class WeatherServiceConnect implements ServiceConnection {

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
}