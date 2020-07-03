package com.geekbrains.myweather.rest;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class OpenWeatherRepo {
    private static OpenWeatherRepo singleton = null;
    private IOpenWeather API;

    private OpenWeatherRepo() {
        API = createAdapter();
    }

    public static OpenWeatherRepo getSingleton() {
        if(singleton == null) {
            singleton = new OpenWeatherRepo();
        }
        return singleton;
    }

    @Provides
    public IOpenWeather getAPI() {
        return API;
    }

    @Provides
    public IOpenWeather createAdapter() {
        Retrofit adapter = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return adapter.create(IOpenWeather.class);
    }
}
