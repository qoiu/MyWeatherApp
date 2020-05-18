package com.geekbrains.myweather.rest.database;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.RoomDatabase;

import com.geekbrains.myweather.rest.dao.WeatherDao;
import com.geekbrains.myweather.rest.model.WeatherInfo;


@Database(entities = {WeatherInfo.class}, version =1,exportSchema = false)
public abstract class DbWeather extends RoomDatabase {
    public abstract WeatherDao getWeatherDao();
}
