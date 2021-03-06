package com.geekbrains.myweather.rest.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.geekbrains.myweather.rest.dao.WeatherDao;
import com.geekbrains.myweather.rest.model.WeatherInfo;


@Database(entities = {WeatherInfo.class}, version =3,exportSchema = false)
public abstract class DbWeather extends RoomDatabase {
    public abstract WeatherDao getWeatherDao();
}
