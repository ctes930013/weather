package com.example.weather.utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地偏好處理相關
 *
 */
public class SharedPrefUtils {

    Context context;
    public SharedPrefUtils(Context context){
        this.context = context;
    }

    //紀錄操作時段是晚上還是白天
    String currentMode = "current_Mode";
    //紀錄天氣的地區模式
    String regionMode = "region_mode";
    //紀錄天氣的縣市
    String regionCounty = "region_county";
    //紀錄天氣的鄉鎮市區
    String regionCity = "region_city";
    //是否變更鄉鎮市區
    String isChangeCity = "is_change_city";

    /**
     * 設定紀錄操作時段
     *
     * @param mode  當前是白天還是晚上，0:白天，1:晚上
     */
    public void setLastMode(int mode){
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        pref.edit()
                .putInt(currentMode, mode)
                .commit();
    }

    /**
     * 取得紀錄操作時段
     *
     * @return int  0:白天，1:晚上
     */
    public int getLastMode(){
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        return pref.getInt(currentMode, 0);
    }

    /**
     * 設定紀錄天氣的地區模式
     *
     * @param mode  當前選用模式，1:GPS，2:手動選擇
     */
    public void setRegionMode(int mode){
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        pref.edit()
                .putInt(regionMode, mode)
                .commit();
    }

    /**
     * 設定紀錄天氣的地區模式
     *
     * @return int  1:GPS，2:手動選擇
     */
    public int getRegionMode(){
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        return pref.getInt(regionMode, 1);
    }

    /**
     * 設定紀錄天氣的縣市
     *
     * @param county  當前選擇縣市
     */
    public void setRegionCounty(String county){
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        pref.edit()
                .putString(regionCounty, county)
                .commit();
    }

    /**
     * 取得紀錄天氣的縣市
     *
     * @return String  縣市
     */
    public String getRegionCounty(){
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        return pref.getString(regionCounty, "");
    }

    /**
     * 設定紀錄天氣的鄉鎮市區
     *
     * @param city  當前選擇鄉鎮市區
     */
    public void setRegionCity(String city){
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        pref.edit()
                .putString(regionCity, city)
                .commit();
    }

    /**
     * 取得紀錄天氣的鄉鎮市區
     *
     * @return String  縣市
     */
    public String getRegionCity(){
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        return pref.getString(regionCity, "");
    }

    /**
     * 設定是否變更鄉鎮市區
     *
     * @param change
     */
    public void setIsChangeCity(boolean change){
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        pref.edit()
                .putBoolean(isChangeCity, change)
                .commit();
    }

    /**
     * 取得是否變更鄉鎮市區
     *
     * @return boolean
     */
    public boolean getIsChangeCity(){
        SharedPreferences pref = context.getSharedPreferences("test", MODE_PRIVATE);
        return pref.getBoolean(isChangeCity, false);
    }
}
