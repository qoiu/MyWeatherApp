package com.geekbrains.myweather.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.Date;

public class AppSettings implements Serializable {
    private static AppSettings instance;
    private boolean settingNightMode;
    private boolean settingWnd;
    private boolean settingPressure;
    private boolean settingHumidity;
    private boolean settingInFahrenheit=false;
    private boolean internet=true;
    private String cityName="";
    private Location location;
    private String localization;
    private long today;

    /**
     * long today - is noon by default
     */
    private AppSettings() {
        today= getBaseToday();
    }

    private long getBaseToday(){
        long now=new Date().getTime()/1000;
        int hour=(int)Math.floor(Integer.parseInt(ConverterDate.extract(now,"HH"))/3f)*3;
        return ConverterDate.getDefineHour(new Date().getTime()/1000,hour);
    }

    public static AppSettings get() {
        if (instance == null) {
            instance = new AppSettings();
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setLocationInLatLng(LatLng latLng) {
        this.location= ConverterLocation.latLngToLocation(latLng);
    }

    public String getLocalization() {
        return localization;
    }

    public void setLocalization(String localization) {
        this.localization = localization;
    }

    public long getToday() {
        return today;
    }

    public void setToday(long today) {
        this.today = today;
    }
}
