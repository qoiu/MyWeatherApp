package com.geekbrains.myweather;

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
        WeatherInfo weatherInfo = weatherDao.getWeather(city, AppSettings.get().getToday());
        if (weatherInfo != null) {
            return weatherInfo;
        }
        return new WeatherInfo();
    }

    public void addCityWeather(WeatherRequestRestModel body, String city) {
        WeatherInfo weather = getWeatherInfo(city);
        weather.cityName = body.cityId.name;
        AppSettings.get().setCityName(weather.cityName);
        weather.latitude = body.cityId.cordRestModel.lat;
        weather.longitude = body.cityId.cordRestModel.lon;
        weather.date = body.listArray[0].dt;
        long today = 0;
        if(!Converter.getHour(weather.date).equals("12")){
            today = weather.date;
            AppSettings.get().setToday(today);
            saveResult(weather,body.listArray[0]);
        }
        for (WeatherListArray weatherElem : body.listArray) {
            String date=Converter.convertDateToString(weatherElem.dt);
            if (Converter.getHour(weatherElem.dt).equals("12")) {
               if(today==0) {
                    today=weatherElem.dt;
                    AppSettings.get().setToday(today);
                }
                saveResult(weather, weatherElem);
            }
        }
    }

    private void saveResult(WeatherInfo weather, WeatherListArray weatherElem) {
        weather.date = weatherElem.dt;
        weather.temperature = weatherElem.weatherMainData.temperature;
        weather.humidity = weatherElem.weatherMainData.humidity;
        weather.pressure = weatherElem.weatherMainData.pressure;
        weather.windSpeed = weatherElem.windRestModel.speed;
        weather.windDirection = weatherElem.windRestModel.deg;
        weather.clouds = weatherElem.weatherExtraData[0].main;
        if (weatherDao.getWeather(weather.cityName, weather.date) != null) {
            weatherDao.updateWeather(weather);
            return;
        }
        weatherDao.insertWeather(weather);
    }

}
