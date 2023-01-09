package com.example.weather.utils;

import com.example.weather.R;

/**
 * 天氣圖片相關處理
 *
 */
public class WeatherImg {

    /**
     * 根據天氣現象取得對應的圖片
     *
     * @param value  Wx的value
     * @return int  R.drawable.sun
     */
    public static int getImgByWeather(int value){
        if (value == 1)
            return R.drawable.sun;
        else if (value >= 2 && value <= 3)
            return R.drawable.cloudy;
        else if (value >= 4 && value <= 7)
            return R.drawable.cloud;
        else if (value >= 8 && value <= 14
                || value >= 19 && value <= 20
                || value >= 29 && value <= 32)
            return R.drawable.rain;
        else if (value >= 15 && value <= 18
                || value >= 21 && value <= 22
                || value >= 33 && value <= 36)
            return R.drawable.storm;
        else if (value == 23 || value == 42)
            return R.drawable.snowy;
        else if (value >= 24 && value <= 28
                || value >= 37 && value <= 41)
            return R.drawable.fog;
        return R.drawable.sun;
    }
}
