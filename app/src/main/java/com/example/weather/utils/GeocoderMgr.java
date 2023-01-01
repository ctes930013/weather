package com.example.weather.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 地圖資訊相關元件
 *
 */
public class GeocoderMgr {

    /**
     * 根據經緯度取得所在地址資訊
     *
     * @param latitude   經度
     * @param longitude  緯度
     * @return List<Address>    相關的地址資訊
     */
    public static List<Address> getLocalAddress(Context context, double latitude, double longitude){
        List<Address> addressesList = new ArrayList<>();
        Geocoder geocoder = new Geocoder(context, Locale.TAIWAN);
        try {
            addressesList = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressesList;
    }

    /**
     * 根據經緯度取得所在鄉鎮市區
     *
     * @param latitude   經度
     * @param longitude  緯度
     * @return String    鄉鎮市區
     */
    public static String getTownName(Context context, double latitude, double longitude){
        List<Address> addressesList = getLocalAddress(context, latitude, longitude);
        if(addressesList.size() > 0)
            return addressesList.get(0).getLocality();
        return Constants.defaultCity;
    }

    /**
     * 根據經緯度取得所在縣市
     *
     * @param latitude   經度
     * @param longitude  緯度
     * @return String    縣市
     */
    public static String getCountyName(Context context, double latitude, double longitude){
        List<Address> addressesList = getLocalAddress(context, latitude, longitude);
        if(addressesList.size() > 0)
            return addressesList.get(0).getAdminArea();
        return Constants.defaultCounty;
    }

    /**
     * 根據當前定位取得經緯度
     *
     * @return Location    經緯度資訊
     */
    public static Location getLocation(Context context){
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        @SuppressLint("MissingPermission") Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        return location;
    }
}
