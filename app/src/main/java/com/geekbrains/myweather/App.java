package com.geekbrains.myweather;


import android.app.Application;

import androidx.room.Room;

import com.geekbrains.myweather.rest.dao.WeatherDao;
import com.geekbrains.myweather.rest.database.DbWeather;

public class App extends Application {
    private static App instance;

    // База данных
    private DbWeather db;

    // Получаем объект приложения
    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Сохраняем объект приложения (для Singleton’а)
        instance = this;

        // Строим базу
        db = Room.databaseBuilder(
                getApplicationContext(),
                DbWeather.class,
                "weather_database")
                .allowMainThreadQueries()
                .build();
    }

    // Получаем EducationDao для составления запросов
    public WeatherDao getEducationDao() {
        return db.getWeatherDao();
    }

    public DbWeather getDatabase(){
        return db;
    }

}
