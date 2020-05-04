package com.geekbrains.myweather;


import android.app.Application;

import androidx.room.Room;

import com.geekbrains.myweather.rest.dao.WeatherDao;
import com.geekbrains.myweather.rest.database.DbWeather;

public class App extends Application {
    private static App instance;

    private DbWeather db;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        db = Room.databaseBuilder(
                getApplicationContext(),
                DbWeather.class,
                "weather_database")
                .allowMainThreadQueries()
                .build();
    }

    public WeatherDao getWeatherDao() {
        return db.getWeatherDao();
    }
}
