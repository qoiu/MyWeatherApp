package com.geekbrains.myweather.presenters.forecastformat;

import com.geekbrains.myweather.rest.model.WeatherInfo;

import java.util.List;

public interface ForecastFormat {
    List<WeatherInfo> format(List<WeatherInfo> fullForecast);
}
