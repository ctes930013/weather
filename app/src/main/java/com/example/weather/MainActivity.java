package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
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
import com.example.weather.model.CityModel;
import com.example.weather.model.WeatherFutureModel;
import com.example.weather.network.APICallback;
import com.example.weather.utils.Constants;
import com.example.weather.utils.DateTimeUtils;
import com.example.weather.utils.GeocoderMgr;
import com.example.weather.utils.Route;
import com.example.weather.utils.SharedPrefUtils;
import com.example.weather.utils.WeatherImg;
import com.gyf.immersionbar.ImmersionBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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


        cityModel = new CityModel();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //取得當前所在的經緯度
        getLocal();
    }

    //取得當前所在位置
    private void getLocal() {
        //沒有權限則返回
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //強制將當前所在地區設定為預設地區
                cityModel.setMyCountry(Constants.defaultCounty);
                cityModel.setMyCity(Constants.defaultCity);
                //呼叫中央氣象局的api
                WeatherApi weatherApi = new WeatherApi(this);
                weatherApi.callWeatherApi(weatherCallback, cityModel.getFutureCodeByCounty(cityModel.getMyCountry()));
                return;
            }
        }

        //取得經緯度
        Location location = GeocoderMgr.getLocation(this);
        if (location != null){
            //成功取得
            //根據經緯度設定當前所在地區
            cityModel.setMyCountry(GeocoderMgr.getCountyName(this, location.getLatitude(), location.getLongitude()));
            cityModel.setMyCity(GeocoderMgr.getTownName(this, location.getLatitude(), location.getLongitude()));
        }else{
            //取得失敗
            Toast.makeText(this, "獲取定位失敗", Toast.LENGTH_SHORT).show();
            //強制將當前所在地區設定為預設地區
            cityModel.setMyCountry(Constants.defaultCounty);
            cityModel.setMyCity(Constants.defaultCity);
        }

        txtTown.setText(cityModel.getMyCountry()+cityModel.getMyCity());

        //呼叫中央氣象局的api
        WeatherApi weatherApi = new WeatherApi(this);
        weatherApi.callWeatherApi(weatherCallback, cityModel.getFutureCodeByCounty(cityModel.getMyCountry()));
    }

    //取得天氣api的回傳值
    private final APICallback<WeatherData> weatherCallback = new APICallback<WeatherData>() {

        @Override
        public void onFailure(int errorCode, String errorMessage) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "獲取天氣資訊失敗", Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onSuccess(WeatherData data) {
            //設定該地區的天氣資訊
            weatherFutureModel.setLocation(data.getWeatherRecord().getLocations().get(0).getLocation());
            weatherFutureModel.setWeatherElements(cityModel.getMyCity());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
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
                }
            });
            Log.d("DATA:", String.valueOf(DateTimeUtils.compareDateTime(DateTimeUtils.getNowTime(),
                    DateTimeUtils.convertStringToDate("2023-01-01 02:02:32"))));
            Log.d("DATA:", weatherFutureModel.getMaxMinRealTempByDate(DateTimeUtils.convertStringToDate("2023-01-10 02:02:32"))[0]);
        }
    };

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
}