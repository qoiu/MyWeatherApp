package com.geekbrains.myweather;


import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.geekbrains.myweather.model.Model;
import com.geekbrains.myweather.rest.dao.WeatherDao;
import com.geekbrains.myweather.rest.database.DbWeather;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class App extends Application {
    private static App instance;
    private static Model model;

    private DbWeather db;

    public static App getInstance() {
        return instance;
    }

    public static Model getModel() {
        return model;
    }

    private static AppComponent component;

   /* @Inject
    Model getModel;*/

    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    private RefWatcher refWatcher;

    @Override
    public void onCreate() {
        super.onCreate();


        instance = this;

        db = Room.databaseBuilder(
                getApplicationContext(),
                DbWeather.class,
                "weather_database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        //LeakCanary.install(this);

        refWatcher = LeakCanary.install(this);

        component= DaggerAppComponent.create();
        //model=new Model();
        model=component.getModel();
        //model=
        Log.w("ss","ss");
    }

    public WeatherDao getWeatherDao() {
        return db.getWeatherDao();
    }
}
