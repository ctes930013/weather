package com.example.weather.model;

import android.util.Log;

import com.example.weather.data.Location;
import com.example.weather.data.WeatherElement;
import com.example.weather.utils.Constants;
import com.example.weather.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 未來一週天氣處理的相關邏輯
 *
 */
public class WeatherFutureModel {

    /**
     * 某縣市的天氣陣列
     *
     */
    private List<Location> location;

    public List<Location> getLocation(){
        return location;
    }

    public void setLocation(List<Location> location) {
        this.location = location;
    }

    /**
     * 12小時降雨機率的天氣陣列
     *
     */
    private List<WeatherElement.Times> rainProb12List;

    public List<WeatherElement.Times> getRainProb12List() {
        return rainProb12List;
    }

    public void setRainProb12List(List<WeatherElement.Times> rainProb12List) {
        this.rainProb12List = rainProb12List;
    }

    /**
     * 天氣現象的天氣陣列
     *
     */
    private List<WeatherElement.Times> phenomenonList;

    public List<WeatherElement.Times> getPhenomenonList() {
        return phenomenonList;
    }

    public void setPhenomenonList(List<WeatherElement.Times> phenomenonList) {
        this.phenomenonList = phenomenonList;
    }

    /**
     * 體感溫度的天氣陣列
     *
     */
    private List<WeatherElement.Times> feelTempList;

    public List<WeatherElement.Times> getFeelTempList() {
        return feelTempList;
    }

    public void setFeelTempList(List<WeatherElement.Times> feelTempList) {
        this.feelTempList = feelTempList;
    }

    /**
     * 實際溫度的天氣陣列
     *
     */
    private List<WeatherElement.Times> realTempList;

    public List<WeatherElement.Times> getRealTempList() {
        return realTempList;
    }

    public void setRealTempList(List<WeatherElement.Times> realTempList) {
        this.realTempList = realTempList;
    }

    /**
     * 相對濕度的天氣陣列
     *
     */
    private List<WeatherElement.Times> humidityList;

    public List<WeatherElement.Times> getHumidityList() {
        return humidityList;
    }

    public void setHumidityList(List<WeatherElement.Times> humidityList) {
        this.humidityList = humidityList;
    }

    /**
     * 舒適度指數的天氣陣列
     *
     */
    private List<WeatherElement.Times> comfortableList;

    public List<WeatherElement.Times> getComfortableList() {
        return comfortableList;
    }

    public void setComfortableList(List<WeatherElement.Times> comfortableList) {
        this.comfortableList = comfortableList;
    }

    /**
     * 天氣預報綜合描述的天氣陣列
     *
     */
    private List<WeatherElement.Times> descList;

    public List<WeatherElement.Times> getDescList() {
        return descList;
    }

    public void setDescList(List<WeatherElement.Times> descList) {
        this.descList = descList;
    }

    /**
     * 6小時降雨機率的天氣陣列
     *
     */
    private List<WeatherElement.Times> rainProb6List;

    public List<WeatherElement.Times> getRainProb6List() {
        return rainProb6List;
    }

    public void setRainProb6List(List<WeatherElement.Times> rainProb6List) {
        this.rainProb6List = rainProb6List;
    }

    /**
     * 風速的天氣陣列
     *
     */
    private List<WeatherElement.Times> windVolList;

    public List<WeatherElement.Times> getWindVolList() {
        return windVolList;
    }

    public void setWindVolList(List<WeatherElement.Times> windVolList) {
        this.windVolList = windVolList;
    }

    /**
     * 風向的天氣陣列
     *
     */
    private List<WeatherElement.Times> windDirectionList;

    public List<WeatherElement.Times> getWindDirectionList() {
        return windDirectionList;
    }

    public void setWindDirectionList(List<WeatherElement.Times> windDirectionList) {
        this.windDirectionList = windDirectionList;
    }

    /**
     * 露點溫度的天氣陣列
     *
     */
    private List<WeatherElement.Times> dewTempList;

    public List<WeatherElement.Times> getDewTempList() {
        return dewTempList;
    }

    public void setDewTempList(List<WeatherElement.Times> dewTempList) {
        this.dewTempList = dewTempList;
    }

    /**
     * 最高溫度的天氣陣列
     *
     */
    private List<WeatherElement.Times> maxTempList;

    public List<WeatherElement.Times> getMaxTempList() {
        return maxTempList;
    }

    public void setMaxTempList(List<WeatherElement.Times> maxTempList) {
        this.maxTempList = maxTempList;
    }

    /**
     * 最低溫度的天氣陣列
     *
     */
    private List<WeatherElement.Times> minTempList;

    public List<WeatherElement.Times> getMinTempList() {
        return minTempList;
    }

    public void setMinTempList(List<WeatherElement.Times> minTempList) {
        this.minTempList = minTempList;
    }

    /**
     * 最高體感溫度的天氣陣列
     *
     */
    private List<WeatherElement.Times> maxRealTempList;

    public List<WeatherElement.Times> getMaxRealTempList() {
        return maxRealTempList;
    }

    public void setMaxRealTempList(List<WeatherElement.Times> maxRealTempList) {
        this.maxRealTempList = maxRealTempList;
    }

    /**
     * 最低體感溫度的天氣陣列
     *
     */
    private List<WeatherElement.Times> minRealTempList;

    public List<WeatherElement.Times> getMinRealTempList() {
        return minRealTempList;
    }

    public void setMinRealTempList(List<WeatherElement.Times> minRealTempList) {
        this.minRealTempList = minRealTempList;
    }

    /**
     * 某個地點的天氣陣列
     *
     */
    private List<WeatherElement> weatherElement;

    public List<WeatherElement> getWeatherElements() {
        return weatherElement;
    }

    /**
     * 根據使用者設定的鄉鎮市區設定該地區的天氣陣列
     * @param city  當前設定的鄉鎮市區
     *
     */
    public void setWeatherElements(String city) {
        List<WeatherElement> weatherElement = getLocation().get(0).getWeatherElements();
        for(int i = 0; i < getLocation().size(); i++){
            String locationName = getLocation().get(i).getLocationName();
            if(locationName.equals(city)){
                //有找到對應的城市了
                Log.d("DATA:", "當前對應的城市: " + locationName);
                weatherElement = getLocation().get(i).getWeatherElements();
                break;
            }
        }
        this.weatherElement = weatherElement;
        this.rainProb12List = getWeatherElementsDetail(Constants.PoP12h);
        this.phenomenonList = getWeatherElementsDetail(Constants.Wx);
        this.feelTempList = getWeatherElementsDetail(Constants.AT);
        this.realTempList = getWeatherElementsDetail(Constants.T);
        this.humidityList = getWeatherElementsDetail(Constants.RH);
        this.comfortableList = getWeatherElementsDetail(Constants.CI);
        this.descList = getWeatherElementsDetail(Constants.WeatherDescription);
        this.rainProb6List = getWeatherElementsDetail(Constants.PoP6h);
        this.windVolList = getWeatherElementsDetail(Constants.WS);
        this.windDirectionList = getWeatherElementsDetail(Constants.WD);
        this.dewTempList = getWeatherElementsDetail(Constants.Td);
        this.maxTempList = getWeatherElementsDetail(Constants.MaxT);
        this.minTempList = getWeatherElementsDetail(Constants.MinT);
        this.maxRealTempList = getWeatherElementsDetail(Constants.MaxAT);
        this.minRealTempList = getWeatherElementsDetail(Constants.MinAT);
    }

    /**
     * 根據天氣陣列取得對應條件的的天氣資訊
     * @param param  天氣條件的參數，例如: 溫度、降雨機率等
     * @return List<WeatherElement.Times>  時段天氣陣列
     *
     */
    private List<WeatherElement.Times> getWeatherElementsDetail(String param) {
        List<WeatherElement.Times> weatherElementDetail = getWeatherElements().get(0).getTime();
        for(int i = 0; i < getWeatherElements().size(); i++){
            String elementName = getWeatherElements().get(i).getElementName();
            if(elementName.equals(param)){
                //有找到對應的條件了
                weatherElementDetail = getWeatherElements().get(i).getTime();
                break;
            }
        }
        return weatherElementDetail;
    }

    /**
     * 獲取當前的溫度
     *
     * @return String  溫度
     *
     */
    public String getNowTemp() {
        return getRealTempList().get(0).getElementValue().get(0).getValue();
    }

    /**
     * 獲取當前的體感溫度
     *
     * @return String  溫度
     *
     */
    public String getNowRealTemp() {
        //直接將最高體感溫度和最低體感溫度取平均
        int temp = (Integer.parseInt(getMaxRealTempList().get(0).getElementValue().get(0).getValue())
                + Integer.parseInt(getMinRealTempList().get(0).getElementValue().get(0).getValue())) / 2;
        return String.valueOf(temp);
    }

    /**
     * 獲取當前的天氣現象概況
     *
     * @return array  ["陰短暫雨", "11"]
     *
     */
    public String [] getNowPhenomenon() {
        return new String[]{getPhenomenonList().get(0).getElementValue().get(0).getValue(),
                getPhenomenonList().get(0).getElementValue().get(1).getValue()};
    }

    /**
     * 獲取當前的風向
     *
     * @return String  風向
     *
     */
    public String getNowWindDirection() {
        return getWindDirectionList().get(0).getElementValue().get(0).getValue();
    }

    /**
     * 獲取當前的天氣描述
     *
     * @return String  描述
     *
     */
    public String getNowDesc() {
        return getDescList().get(0).getElementValue().get(0).getValue();
    }

    /**
     * 獲取當前的降雨機率
     *
     * @return String  降雨機率
     *
     */
    public String getNowRainProb() {
        return getRainProb6List().get(0).getElementValue().get(0).getValue();
    }

    /**
     * 獲取某幾筆最近的溫度
     *
     * @param count  資料比數
     * @return List<Map<String, String>>  溫度陣列
     *
     */
    public List<Map<String, String>> getTempList(int count) {
        List<Map<String, String>> tempList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("start_time", getRealTempList().get(i).getStartTime());
            map.put("end_time", getRealTempList().get(i).getEndTime());
            map.put("temp", getRealTempList().get(i).getElementValue().get(0).getValue());
            tempList.add(map);
        }
        return tempList;
    }

    /**
     * 獲取某幾筆最近的天氣現象概況
     *
     * @param count  資料比數
     * @return List<String []>  ["陰短暫雨", "11"]
     *
     */
    public List<String []> getPhenomenonList(int count) {
        List<String []> phenomenonList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            phenomenonList.add(new String[]{getPhenomenonList().get(0).getElementValue().get(0).getValue(),
                    getPhenomenonList().get(0).getElementValue().get(1).getValue()});
        }
        return phenomenonList;
    }

    /**
     * 獲取某幾筆最近的降雨機率
     *
     * @param count  資料比數
     * @return List<String>  溫度陣列
     *
     */
    public List<String> getRainProbList(int count) {
        List<String> rainList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            rainList.add(getRainProb12List().get(i).getElementValue().get(0).getValue());
        }
        return rainList;
    }

    /**
     * 獲取某日最高最低溫度
     *
     * @param datetime  2023-01-01 00:00:00
     * @return array  ["19", "12"]
     *
     */
    public String [] getMaxMinTempByDate(Date datetime) {
        int maxTemp = -9999;
        int minTemp = 10000;
        String date = DateTimeUtils.getDateByDatetime(datetime);
        //找最高溫度
        List<WeatherElement.Times> maxTempList = getMaxTempList();
        for (int i = 0; i < maxTempList.size(); i++){
            String temp = maxTempList.get(i).getElementValue().get(0).getValue();
            if(maxTempList.get(i).getEndTime().contains(date)
                    || maxTempList.get(i).getStartTime().contains(date)){
                if(maxTemp < Integer.parseInt(temp)){
                    maxTemp = Integer.parseInt(temp);
                }
            }
        }
        //找最低溫度
        List<WeatherElement.Times> mimTempList = getMinTempList();
        for (int i = 0; i < mimTempList.size(); i++){
            String temp = mimTempList.get(i).getElementValue().get(0).getValue();
            if(mimTempList.get(i).getEndTime().contains(date)
                    || mimTempList.get(i).getStartTime().contains(date)){
                if(minTemp > Integer.parseInt(temp)){
                    minTemp = Integer.parseInt(temp);
                }
            }
        }
        return new String[]{String.valueOf(maxTemp), String.valueOf(minTemp)};
    }

    /**
     * 獲取某日最高最低體感溫度
     *
     * @param datetime  2023-01-01 00:00:00
     * @return array  ["19", "12"]
     *
     */
    public String [] getMaxMinRealTempByDate(Date datetime) {
        int maxTemp = -9999;
        int minTemp = 10000;
        String date = DateTimeUtils.getDateByDatetime(datetime);
        //找最高溫度
        List<WeatherElement.Times> maxRealTempList = getMaxRealTempList();
        for (int i = 0; i < maxRealTempList.size(); i++){
            String temp = maxRealTempList.get(i).getElementValue().get(0).getValue();
            if(maxRealTempList.get(i).getEndTime().contains(date)
                    || maxRealTempList.get(i).getStartTime().contains(date)){
                if(maxTemp < Integer.parseInt(temp)){
                    maxTemp = Integer.parseInt(temp);
                }
            }
        }
        //找最低溫度
        List<WeatherElement.Times> mimRealTempList = getMinRealTempList();
        for (int i = 0; i < mimRealTempList.size(); i++){
            String temp = mimRealTempList.get(i).getElementValue().get(0).getValue();
            if(mimRealTempList.get(i).getEndTime().contains(date)
                    || mimRealTempList.get(i).getStartTime().contains(date)){
                if(minTemp > Integer.parseInt(temp)){
                    minTemp = Integer.parseInt(temp);
                }
            }
        }
        return new String[]{String.valueOf(maxTemp), String.valueOf(minTemp)};
    }

    /**
     * 獲取某日天氣現象概況
     *
     * @param datetime  2023-01-01 00:00:00
     * @return array  ["陰短暫雨", "11"]
     *
     */
    public String [] getPhenomenonByDate(Date datetime) {
        String [] phenomenon = new String[2];
        String date = DateTimeUtils.getDateByDatetime(datetime);
        List<WeatherElement.Times> phenomenonList = getPhenomenonList();
        for (int i = 0; i < phenomenonList.size(); i++){
            if(phenomenonList.get(i).getEndTime().contains(date)
                    || phenomenonList.get(i).getStartTime().contains(date)){
                phenomenon[0] = phenomenonList.get(i).getElementValue().get(0).getValue();
                phenomenon[1] = phenomenonList.get(i).getElementValue().get(1).getValue();
                break;
            }
        }
        return phenomenon;
    }

    /**
     * 獲取某日降雨機率
     *
     * @param datetime  2023-01-01 00:00:00
     * @return Map<String, String>  {2023-01-03 06:00:00=60, 2023-01-03 18:00:00=60}  前面的key是開始統計時間，後面是降雨機率
     *
     */
    public Map<String, String> getRainProbByDate(Date datetime) {
        Map<String, String> map = new HashMap<>();
        String date = DateTimeUtils.getDateByDatetime(datetime);
        List<WeatherElement.Times> rainProbList = getRainProb12List();
        for (int i = 0; i < rainProbList.size(); i++){
            if(rainProbList.get(i).getStartTime().contains(date)){
                map.put(rainProbList.get(i).getStartTime(), rainProbList.get(i).getElementValue().get(0).getValue());
            }
        }
        return map;
    }

    /**
     * 獲取某日最大降雨機率
     *
     * @param datetime  2023-01-01 00:00:00
     * @return int 60
     *
     */
    public int getMaxRainProbByDate(Date datetime) {
        Map<String, String> map = getRainProbByDate(datetime);
        int maxProb = 0;
        Set keySet = map.keySet();
        //有了set集合就可以獲取迭代器
        for (Object o : keySet) {
            String key = String.valueOf(o);
            //有了鍵就可以通過map集合的get方法獲取其對應的値
            String val = map.get(key);
            if(" ".equals(val))
                val = "0";
            int value = Integer.parseInt(val);
            if (value >= maxProb)
                maxProb = value;
        }
        return maxProb;
    }
}
