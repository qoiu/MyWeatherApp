package com.geekbrains.myweather.rest.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.geekbrains.myweather.SettingsSingleton;

import java.util.Locale;

@Entity(indices = {@Index(value = {
        WeatherInfo.CITY_NAME,
        WeatherInfo.DATE,
        WeatherInfo.TEMPERATURE,
        WeatherInfo.HUMIDITY,
        WeatherInfo.PRESSURE,
        WeatherInfo.WIND_SPEED,
        WeatherInfo.WIND_DIRECTION,
        WeatherInfo.CLOUDS
}
        )})
public class WeatherInfo {
    private final static String ID="id";
    final static String CITY_NAME="city_name";
    final static String DATE = "date";
    final static String TEMPERATURE="temperature";
    final static String HUMIDITY="humidity";
    final static String PRESSURE="pressure";
    final static String WIND_SPEED="wind_speed";
    final static String WIND_DIRECTION="wind_direction";
    final static String CLOUDS="clouds";

    @PrimaryKey(autoGenerate = true)@ColumnInfo(name = ID) public long id;
    @ColumnInfo(name = CITY_NAME) public String cityName;
    @ColumnInfo(name = DATE) public long date;
    @ColumnInfo(name = TEMPERATURE) public float temperature;
    @ColumnInfo(name = HUMIDITY) public int humidity ;
    @ColumnInfo(name = PRESSURE) public int pressure;
    @ColumnInfo(name = WIND_SPEED) public float windSpeed;
    @ColumnInfo(name = WIND_DIRECTION) public int windDirection;
    @ColumnInfo(name = CLOUDS) public String clouds;

    public String getTemperature() {
        if(!SettingsSingleton.getInstance().isSettingInFahrenheit()){
            return String.format(Locale.getDefault(), "%+.1f" +
                    "\u2103", temperature);
        }else {
            double temperatureFahr=temperature*9/5+32;
            return String.format(Locale.getDefault(), "%+.1f"+
                    "\u2109", temperatureFahr);
        }
    }
}
