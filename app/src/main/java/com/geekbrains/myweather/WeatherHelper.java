package com.geekbrains.myweather;

import com.geekbrains.myweather.model.AppSettings;
import com.geekbrains.myweather.rest.dao.WeatherDao;
import com.geekbrains.myweather.rest.entities.WeatherListArray;
import com.geekbrains.myweather.rest.entities.WeatherRequestRestModel;
import com.geekbrains.myweather.rest.model.WeatherInfo;
import com.google.android.gms.maps.model.LatLng;

public class WeatherHelper {
    private final WeatherDao weatherDao;

    public WeatherHelper(WeatherDao weatherDao) {
        this.weatherDao = weatherDao;
    }

    private WeatherInfo getWeatherInfo(String city,long dt) {
        WeatherInfo weatherInfo = weatherDao.getWeather(city,dt);
        if (weatherInfo != null) {
            return weatherInfo;
        }
        return new WeatherInfo();
    }
    /**
     * 3 часа - 10800
     */
    public void addCityWeather(WeatherRequestRestModel body) {
        String city = body.cityId.name;
        AppSettings.get().setCityName(city);
        float lat = body.cityId.cordRestModel.lat;
        float lon = body.cityId.cordRestModel.lon;
        AppSettings.get().setLocationInLatLng(new LatLng(lat,lon));
        AppSettings.get().setToday(body.listArray[0].dt);
        for (WeatherListArray weatherElem : body.listArray) {
            WeatherInfo weather = getWeatherInfo(city,weatherElem.dt);
            weather.cityName=city;
            weather.longitude=lon;
            weather.latitude=lat;
            saveResult(weather, weatherElem);
        }
    }

    private void saveResult(WeatherInfo weather, WeatherListArray weatherElem) {
        weather.temperature = weatherElem.weatherMainData.temperature;
        weather.humidity = weatherElem.weatherMainData.humidity;
        weather.pressure = weatherElem.weatherMainData.pressure;
        weather.windSpeed = weatherElem.windRestModel.speed;
        weather.windDirection = weatherElem.windRestModel.deg;
        weather.clouds = weatherElem.weatherExtraData[0].main;
        WeatherInfo weatherInfo=weatherDao.getWeather(weather.cityName, weather.date);
        if (weather.id!= 0) {
            weather.id=weatherInfo.id;
            weatherDao.updateWeather(weather);
            return;
        }
        weather.date = weatherElem.dt;
        weatherDao.insertWeather(weather);
    }

}
