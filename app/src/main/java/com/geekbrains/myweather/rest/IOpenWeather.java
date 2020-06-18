package com.geekbrains.myweather.rest;

import com.geekbrains.myweather.rest.entities.WeatherRequestRestModel;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface IOpenWeather {
    @Streaming
    @GET("data/2.5/forecast")
    Observable<Response<WeatherRequestRestModel>> loadWeather(@Query("q") String city,
                                                              @Query("appid") String keyApi,
                                                              @Query("units") String units,
                                                              @Query("lang") String lang);

    @Streaming
    @GET("data/2.5/forecast")
    Observable<Response<WeatherRequestRestModel>> loadWeatherFromGeo(@Query("lat") double latitude,
                                                     @Query("lon") double longitude,
                                                     @Query("appid") String keyApi,
                                                     @Query("units") String units,
                                                     @Query("lang") String lang);
}
