package com.geekbrains.myweather;

import java.io.Serializable;

public class Singleton implements Serializable {
    private static Singleton instance;
    private boolean settingNightMode;
    private boolean settingWnd;
    private boolean settingPressure;
    private boolean settingHumidity;
    private boolean settingInCelsius=true;
    private CityData city;
    private String cityName="Москва";

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void setSettingNightMode(boolean settingNightMode) {
        this.settingNightMode = settingNightMode;
    }

    public void setSettingWnd(boolean settingWnd) {
        this.settingWnd = settingWnd;
    }

    public void setSettingPressure(boolean settingPressure) {
        this.settingPressure = settingPressure;
    }

    public void setSettingHumidity(boolean settingHumidity) {
        this.settingHumidity = settingHumidity;
    }

    public void setSettingInCelsius(boolean settingInCels) {
        this.settingInCelsius = settingInCels;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isSettingNightMode() {
        return settingNightMode;
    }

    public boolean isSettingWnd() {
        return settingWnd;
    }

    public boolean isSettingPressure() {
        return settingPressure;
    }

    public boolean isSettingHumidity() {
        return settingHumidity;
    }

    public boolean isSettingInCelsius() {
        return settingInCelsius;
    }

    public String getCityName() {
        return cityName;
    }

    public CityData getCity() {
        return city;
    }

    public void setCity(CityData city) {
        this.city = city;
    }
}
