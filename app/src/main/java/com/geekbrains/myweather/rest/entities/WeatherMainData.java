package com.geekbrains.myweather.rest.entities;

import com.geekbrains.myweather.model.AppSettings;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class WeatherMainData {
    @SerializedName("temp")public float temperature;
    @SerializedName("feels_like")public float feelsLike;
    @SerializedName("temp_min")public float tempMin;
    @SerializedName("temp_max")public float tempMax;
    @SerializedName("pressure")public int pressure;
    @SerializedName("sea_level")public int seaLevel;
    @SerializedName("grnd_level")public int groundLevel;
    @SerializedName("humidity")public int humidity;

    public String getTemperature() {
        if(!AppSettings.get().isSettingInFahrenheit()){
            return String.format(Locale.getDefault(), "%+.1f" +
                    "\u2103", temperature);
        }else {
            double temperatureFahr=temperature*9/5+32;
            return String.format(Locale.getDefault(), "%+.1f"+
                    "\u2109", temperatureFahr);
        }
    }

    public String getPressure() {
        return String.format(Locale.getDefault(), "%+.1f%n", pressure / 1.33333f);
    }

    public String getHumidity() {
        return humidity + "%";
    }
}
