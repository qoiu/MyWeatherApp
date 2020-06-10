package com.geekbrains.myweather.rest.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.geekbrains.myweather.AppSettings;
import com.geekbrains.myweather.Weather;
import java.util.Locale;

@Entity(indices = {
        @Index(value = {
                WeatherInfo.CITY_NAME,
                WeatherInfo.DATE,
                WeatherInfo.TEMPERATURE,
                WeatherInfo.HUMIDITY,
                WeatherInfo.PRESSURE,
                WeatherInfo.WIND_SPEED,
                WeatherInfo.WIND_DIRECTION,
                WeatherInfo.CLOUDS,
                WeatherInfo.LATITUDE,
                WeatherInfo.LONGITUDE
        }
        )})
public class WeatherInfo {
    private final static String ID = "id";
    final static String CITY_NAME = "city_name";
    final static String DATE = "date";
    final static String TEMPERATURE = "temperature";
    final static String HUMIDITY = "humidity";
    final static String PRESSURE = "pressure";
    final static String WIND_SPEED = "wind_speed";
    final static String WIND_DIRECTION = "wind_direction";
    final static String CLOUDS = "clouds";
    final static String LATITUDE = "latitude";
    final static String LONGITUDE = "longitude";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public long id;
    @ColumnInfo(name = CITY_NAME)
    public String cityName;
    @ColumnInfo(name = DATE)
    public long date;
    @ColumnInfo(name = TEMPERATURE)
    public float temperature;
    @ColumnInfo(name = HUMIDITY)
    public int humidity;
    @ColumnInfo(name = PRESSURE)
    public int pressure;
    @ColumnInfo(name = WIND_SPEED)
    public float windSpeed;
    @ColumnInfo(name = WIND_DIRECTION)
    public int windDirection;
    @ColumnInfo(name = CLOUDS)
    public String clouds;
    @ColumnInfo(name = LATITUDE)
    public float latitude;
    @ColumnInfo(name = LONGITUDE)
    public float longitude;

    public String getTemperatureString() {
        if (!AppSettings.get().isSettingInFahrenheit()) {
            return String.format(Locale.getDefault(), "%+.1f" +
                    "\u2103", temperature);
        } else {
            double temperatureFahr = temperature * 9 / 5 + 32;
            return String.format(Locale.getDefault(), "%+.1f" +
                    "\u2109", temperatureFahr);
        }
    }

    public String getWindDirection() {
        return Weather.getWindDirection(windDirection);
    }

    public String getWindSpeed() {
        return String.format(Locale.getDefault(), "%.1f %s", windSpeed,"м/c");
    }
}
