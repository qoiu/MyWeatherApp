package com.geekbrains.myweather.rest;

import com.geekbrains.myweather.rest.entities.WeatherRequestRestModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeather {
    @GET("data/2.5/forecast")
    Call<WeatherRequestRestModel> loadWeather(@Query("q") String city,
                                              @Query("appid") String keyApi,
                                              @Query("units") String units);
    @GET("data/2.5/forecast")
    Call<WeatherRequestRestModel> loadWeatherFromGeo(@Query("lat") double latitude,
                                                     @Query("lon") double longitude,
                                                     @Query("appid") String keyApi,
                                                     @Query("units") String units);
}
