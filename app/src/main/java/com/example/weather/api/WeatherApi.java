package com.example.weather.api;

import android.content.Context;

import com.example.weather.R;
import com.example.weather.data.WeatherData;
import com.example.weather.network.APICallback;
import com.example.weather.network.APIRequest;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * 取得天氣資訊API處理相關
 *
 */
public class WeatherApi {

    private final Context context;

    public WeatherApi(Context context){
        this.context = context;
    }

    /**
     * 呼叫取得天氣資訊的api
     *
     * @param callback  api回傳的資料
     * @param countryCode  縣市代碼
     */
    public void callWeatherApi(APICallback<WeatherData> callback, String countryCode){
        Map<String, Object> map = new HashMap<>();
        map.put("Authorization", context.getResources().getString(R.string.weather_api_key));
        map.put("format", "JSON");
        APIRequest.getInstance().sendGET(APIUrl.weatherUrl + countryCode, map, new APICallback<String>() {

            @Override
            public void onFailure(int errorCode, String errorMessage) {
                callback.onFailure(errorCode, errorMessage);
            }

            @Override
            public void onSuccess(String data) {
                Gson gson = new Gson();
                WeatherData weatherData = gson.fromJson(data, WeatherData.class);
                callback.onSuccess(weatherData);
            }
        });
    }
}
