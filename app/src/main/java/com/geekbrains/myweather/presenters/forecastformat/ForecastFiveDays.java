package com.geekbrains.myweather.presenters.forecastformat;

import com.geekbrains.myweather.model.ConverterDate;
import com.geekbrains.myweather.rest.model.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

public class ForecastFiveDays implements ForecastFormat {
    @Override
    public List<WeatherInfo> format(List<WeatherInfo> fullForecast) {
        if(fullForecast == null || fullForecast.size()<1) return null;
        List<WeatherInfo> tomorrowForecast=new ArrayList<>();
        for (int i=0;i<fullForecast.size();i++){
            if(ConverterDate.isNoon(fullForecast.get(i).date)){
                tomorrowForecast.add(fullForecast.get(i));
                fullForecast.get(i).setTitle(ConverterDate.extract(fullForecast.get(i).date,"dd.MM"));
            }
        }
        return tomorrowForecast;
    }
}
