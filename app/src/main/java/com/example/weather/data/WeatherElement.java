package com.example.weather.data;

import java.util.List;

public class WeatherElement {

    public String elementName;    //天氣因子代碼
    public String description;    //天氣因子描述
    private List<Times> time;      //各時段天氣

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Times> getTime() {
        return time;
    }

    public void setTime(List<Times> time) {
        this.time = time;
    }

    public static class Times {
        public String startTime;    //開始時間
        public String endTime;    //結束時間
        public List<ElementValue> elementValue;    //天氣概況

        public String getStartTime() {
            return startTime;
        }

        public void setElementName(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public List<ElementValue> getElementValue() {
            return elementValue;
        }

        public void setElementValue(List<ElementValue> elementValue) {
            this.elementValue = elementValue;
        }
    }
}
