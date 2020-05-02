package com.geekbrains.myweather;

import java.io.Serializable;

public class SettingsSingleton implements Serializable {
    private static SettingsSingleton instance;
    private boolean settingNightMode;
    private boolean settingWnd;
    private boolean settingPressure;
    private boolean settingHumidity;
    private boolean settingInFahrenheit=false;
    private boolean internet=true;
    private String cityName="";

    private SettingsSingleton() {
    }

    public static SettingsSingleton getInstance() {
        if (instance == null) {
            instance = new SettingsSingleton();
        }
        return instance;
    }

    public boolean isInternet() {
        return internet;
    }

    public void setInternet(boolean internet) {
        this.internet = internet;
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

    public void setSettingInFahrenheit(boolean settingInCels) {
        this.settingInFahrenheit = settingInCels;
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

    public boolean isSettingInFahrenheit() {
        return settingInFahrenheit;
    }

    public String getCityName() {
        return cityName;
    }
}
