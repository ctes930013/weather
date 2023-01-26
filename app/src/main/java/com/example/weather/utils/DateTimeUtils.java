package com.example.weather.utils;

import android.content.Context;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期時間處理相關
 *
 */
public class DateTimeUtils {

    /**
     * 取得當前日期時間
     *
     * @return Date  Sun Jan 01 04:50:25 GMT 2023
     */
    public static Date getNowTime(){
        return Calendar.getInstance().getTime();
    }

    /**
     * 日期字串轉換為Date型態
     *
     * @param datetime  欲轉換的日期時間    2023-01-01 04:50:25
     * @return Date  Sun Jan 01 04:50:25 GMT 2023
     */
    public static Date convertStringToDate(String datetime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN);
        try {
            return sdf.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("Date Error: ", e.getMessage());
            return Calendar.getInstance().getTime();
        }
    }

    /**
     * 比較日期時間大小
     *
     * @param time1  日期時間    2023-01-01 04:50:25
     * @param time2  日期時間    2023-01-01 04:50:25
     * @return boolean  倘若前面參數時間較大回傳true，反之回傳false
     */
    public static boolean compareDateTime(Date time1, Date time2){
        return time1.getTime() > time2.getTime();
    }

    /**
     * 根據日期時間取得日期
     *
     * @param datetime  日期時間    Sun Jan 01 04:50:25 GMT 2023
     * @return String  2023-01-01
     */
    public static String getDateByDatetime(Date datetime){
        return convertDistinctFormat("yyyy-MM-dd", datetime);
    }

    /**
     * 取得當前日期往後加上幾天的日期
     *
     * @param count  要加上幾天
     * @return String  2023-01-01
     */
    public static String getAddDistinctDate(int count){
        Calendar calendar = Calendar.getInstance();

        calendar.add(Calendar.DAY_OF_YEAR, count);
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.TAIWAN);
        return sdf.format(date);
    }

    /**
     * 將日期轉換為特定格式
     *
     * @param format  yyyy-MM-dd
     * @param datetime  日期時間    Sun Jan 01 04:50:25 GMT 2023
     * @return String  2023-01-01
     */
    public static String convertDistinctFormat(String format, Date datetime){
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.TAIWAN);
        return sdf.format(datetime);
    }

    /**
     * 根據日期取得星期幾
     *
     * @param datetime  日期時間    Sun Jan 01 04:50:25 GMT 2023
     * @return String  星期日
     */
    public static String getWeekOfDate(Date datetime) {
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(datetime);
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 根據時段選擇背景顏色
     *
     */
    public static void setCurrentMode(Context context){
        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(context);
        int hours = Integer.parseInt(convertDistinctFormat("HH", getNowTime()));
        if (hours >= 6 && hours <= 18) {
            sharedPrefUtils.setLastMode(0);
        } else {
            sharedPrefUtils.setLastMode(1);
        }
    }
}
