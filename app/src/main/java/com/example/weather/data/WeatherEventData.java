package com.example.weather.data;

/**
 * 天氣api回傳的基本資料用作EventBus傳輸資料用
 *
 */
public class WeatherEventData {

    //api回傳code
    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    //錯誤資訊
    private String errorData;

    public String getErrorData() {
        return errorData;
    }

    public void setErrorData(String errorData) {
        this.errorData = errorData;
    }

    //天氣的data
    private WeatherData weatherData;

    public WeatherData getWeatherData() {
        return weatherData;
    }

    public void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }
}
