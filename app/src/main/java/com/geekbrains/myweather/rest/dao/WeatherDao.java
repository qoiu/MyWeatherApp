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

    @Query("SELECT * FROM weatherinfo ORDER BY temperature ASC ")
    List<WeatherInfo> getSortedTemperature();


    @Query("SELECT * FROM weatherinfo ORDER BY date ASC ")
    List<WeatherInfo> getSortedDate();

    @Query("SELECT * FROM weatherinfo ORDER BY city_name ASC ")
    List<WeatherInfo> getSortedCityName();
}
