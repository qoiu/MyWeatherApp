package com.geekbrains.myweather;

import android.location.Location;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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

    private AppSettings() {
        today= getBaseToday();
    }

    private long getBaseToday(){
        return Converter.getNoon(new Date().getTime()/1000);
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
        this.location=Converter.latLngToLocation(latLng);
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
