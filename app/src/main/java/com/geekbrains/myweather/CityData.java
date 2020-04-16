package com.geekbrains.myweather;

import java.io.Serializable;
import java.util.ArrayList;

public class CityData implements Serializable {
    private String cityName;
    private WeatherInfo todayInfo;
    private ArrayList<WeatherInfo> forecastInfo;
    private boolean exist;

    public CityData(String cityName){
        this.cityName=cityName;
        forecastInfo=new ArrayList<WeatherInfo>();
    }

    void addWeather(WeatherInfo weatherInfo){
        forecastInfo.add(weatherInfo);
    }

    WeatherInfo getForecastInfo(int day) {
        return forecastInfo.get(day);
    }

    WeatherInfo[] getWeatherListByDay(){
        WeatherInfo[] arr=new WeatherInfo[forecastInfo.size()];
        for(int i=0;i<forecastInfo.size();i++){
            arr[i]= forecastInfo.get(i);
        }
        return arr;
    }

    public String getCityName() {
        return cityName;
    }

    public WeatherInfo getTodayInfo() {
        return todayInfo;
    }

    public void setTodayInfo(WeatherInfo todayInfo) {
        this.todayInfo = todayInfo;
    }

    public boolean isExist() {
        return exist;
    }

    public void setExist(boolean exist) {
        this.exist = exist;
    }
}
