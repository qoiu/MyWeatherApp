package com.geekbrains.myweather.rest.entities;

import com.geekbrains.myweather.Weather;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class WindRestModel {
    @SerializedName("speed")public float speed;
    @SerializedName("deg")public int deg;

    public String getWindDirection() {
        return Weather.getWindDirection(deg);
    }

    public String getWindSpeed() {
        return String.format(Locale.getDefault(), "%.1f %s", speed,"м/c");
    }

}
