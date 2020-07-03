package com.geekbrains.myweather.presenters.forecastformat;

import com.geekbrains.myweather.model.ConverterDate;
import com.geekbrains.myweather.rest.model.WeatherInfo;

import java.util.List;

public class ForecastToday implements ForecastFormat {
    @Override
    public List<WeatherInfo> format(List<WeatherInfo> fullForecast) {
        if(fullForecast == null || fullForecast.size()<1) return null;
        for (int i=0;i<fullForecast.size();i++){
            if (i>7){
                fullForecast.remove(i);
                i--;
            } else {
                ConverterDate.formatTitle(fullForecast.get(i),false);
            }
        }
        ConverterDate.formatTitle(fullForecast.get(0),true);
        return fullForecast;
    }
}
