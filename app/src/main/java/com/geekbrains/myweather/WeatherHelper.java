package com.geekbrains.myweather;

import android.util.Log;

import com.geekbrains.myweather.rest.dao.WeatherDao;
import com.geekbrains.myweather.rest.entities.WeatherListArray;
import com.geekbrains.myweather.rest.entities.WeatherRequestRestModel;
import com.geekbrains.myweather.rest.model.WeatherInfo;

public class WeatherHelper {
    private final WeatherDao weatherDao;

    public WeatherHelper(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    private WeatherInfo getWeatherInfo(String city) {
        WeatherInfo weatherInfo = weatherDao.getWeather(city);
        if (weatherInfo != null) {
            return weatherInfo;
        }
        return new WeatherInfo();
    }

    public void addCityWeather(WeatherRequestRestModel body) {
        boolean update = weatherDao.getWeather(body.cityId.name) != null;
        WeatherInfo weather = getWeatherInfo(body.cityId.name);
        weather.cityName = body.cityId.name;
        weather.date = body.listArray[0].dt;
        WeatherListArray weatherData;
        if (Integer.parseInt(body.listArray[0].getHour()) >= 12) {
            weatherData = body.listArray[0];
        } else {
            int i = 0;
            while (Integer.parseInt(body.listArray[i].getHour()) < 12) {
                i++;
            }
            weatherData = body.listArray[i];
        }
        weather.temperature = weatherData.weatherMainData.temperature;
        weather.humidity = weatherData.weatherMainData.humidity;
        weather.pressure = weatherData.weatherMainData.pressure;
        weather.windSpeed = weatherData.windRestModel.speed;
        weather.windDirection = weatherData.windRestModel.deg;
        weather.clouds=weatherData.weatherExtraData[0].main;
        if (update) {
            Log.d("Helper","update");
            weatherDao.updateWeather(weather);
        } else {
            Log.d("Helper","insert");
            weatherDao.insertWeather(weather);
        }
    }
}
