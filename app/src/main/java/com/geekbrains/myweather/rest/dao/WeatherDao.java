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

    @Query("SELECT * FROM weatherinfo WHERE city_name = :city")
    WeatherInfo getWeather(String city);

    @Query("SELECT * FROM weatherinfo")
    List<WeatherInfo> getAllCities();

    @Query("SELECT * FROM weatherinfo ORDER BY CASE WHEN :isAsc = 1 THEN temperature END ASC, CASE WHEN :isAsc = 0 THEN temperature END DESC ")
    List<WeatherInfo> getSortedTemperature(boolean isAsc);

    @Query("SELECT * FROM weatherinfo ORDER BY CASE WHEN :isAsc = 1 THEN date END ASC, CASE WHEN :isAsc = 0 THEN date END DESC")
    List<WeatherInfo> getSortedDate(boolean isAsc);

    @Query("SELECT * FROM weatherinfo ORDER BY CASE WHEN :isAsc = 1 THEN city_name END ASC, CASE WHEN :isAsc = 0 THEN city_name END DESC ")
    List<WeatherInfo> getSortedCityName(boolean isAsc);
}
