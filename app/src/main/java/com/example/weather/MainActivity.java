package com.example.weather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.weather.api.WeatherApi;
import com.example.weather.data.WeatherData;
import com.example.weather.model.CityModel;
import com.example.weather.model.WeatherFutureModel;
import com.example.weather.network.APICallback;
import com.example.weather.utils.Constants;
import com.example.weather.utils.DateTimeUtils;
import com.example.weather.utils.GeocoderMgr;

public class MainActivity extends AppCompatActivity {

    private CityModel cityModel;
    private WeatherFutureModel weatherFutureModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityModel = new CityModel();
        weatherFutureModel = new WeatherFutureModel();

        //權限檢查
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }
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
            Log.d("DATA:", String.valueOf(DateTimeUtils.compareDateTime(DateTimeUtils.getNowTime(),
                    DateTimeUtils.convertStringToDate("2023-01-01 02:02:32"))));
            Log.d("DATA:", weatherFutureModel.getMaxMinRealTempByDate(DateTimeUtils.convertStringToDate("2023-01-04 02:02:32"))[0]);
        }
    };
}