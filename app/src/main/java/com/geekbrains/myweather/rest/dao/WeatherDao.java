package com.geekbrains.myweather.rest.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.geekbrains.myweather.rest.model.WeatherInfo;

import java.util.List;

@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertWeather(WeatherInfo weatherInfo);

    @Update
    void updateWeather(WeatherInfo weatherInfo);

    @Query("SELECT * FROM weatherinfo WHERE city_name = :city AND date=:date")
    WeatherInfo getWeather(String city,long date);

    @Query("SELECT * FROM weatherinfo")
    List<WeatherInfo> getAllCities();

    @Query("SELECT * FROM (SELECT * FROM WeatherInfo WHERE date>=:today ORDER BY date DESC)GROUP BY city_name ORDER BY CASE WHEN :isAsc = 1 THEN temperature END ASC, CASE WHEN :isAsc = 0 THEN temperature END DESC ")
    List<WeatherInfo> getSortedTemperature(boolean isAsc,Long today);

    @Query("SELECT * FROM (SELECT * FROM WeatherInfo WHERE date>=:today ORDER BY date DESC)GROUP BY city_name ORDER BY CASE WHEN :isAsc = 1 THEN date END ASC, CASE WHEN :isAsc = 0 THEN date END DESC")
    List<WeatherInfo> getSortedDate(boolean isAsc,Long today);

    @Query("SELECT * FROM (SELECT * FROM WeatherInfo WHERE date>=:today ORDER BY date DESC)GROUP BY city_name ORDER BY CASE WHEN :isAsc = 1 THEN city_name END ASC, CASE WHEN :isAsc = 0 THEN city_name END DESC ")
    List<WeatherInfo> getSortedCityName(boolean isAsc,Long today);

    @Query("SELECT * FROM (SELECT * FROM WeatherInfo WHERE city_name=:city ORDER BY date DESC LIMIT 6) ORDER BY date;")
    List<WeatherInfo> getForecast(String city);


    @Query("SELECT * FROM (SELECT * FROM WeatherInfo WHERE city_name=:city AND date>=:now ORDER BY date ASC) ORDER BY date;")
    List<WeatherInfo> getForecastFromNow(String city,long now);


    @Query("SELECT * FROM (SELECT * FROM WeatherInfo WHERE city_name=:city ORDER BY date DESC) ORDER BY date;")
    List<WeatherInfo> getForecastTest(String city);

    @Query("SELECT * FROM (SELECT * FROM WeatherInfo WHERE city_name=:city AND date>=:today ORDER BY date DESC) ORDER BY date ASC LIMIT 1;")
    WeatherInfo getCity(String city,Long today);
}
