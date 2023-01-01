package com.example.weather.data;

import java.util.List;

public class Location {

    public String locationName;    //鄉鎮市區
    public String geocode;
    public String lat;    //緯度
    public String lon;    //經度
    public List<WeatherElement> weatherElement;

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getGeocode() {
        return geocode;
    }

    public void setGeocode(String geocode) {
        this.geocode = geocode;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public List<WeatherElement> getWeatherElements() {
        return weatherElement;
    }

    public void setWeatherElements(List<WeatherElement> weatherElement) {
        this.weatherElement = weatherElement;
    }
}
