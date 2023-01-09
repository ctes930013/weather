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
}
