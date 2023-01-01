package com.example.weather.data;

public class ElementValue {

    public String value;    //氣溫or降雨量的預估值
    public String measures;    //單位

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getMeasures() {
        return measures;
    }

    public void setMeasures(String measures) {
        this.measures = measures;
    }
}
