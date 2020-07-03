package com.geekbrains.myweather.presenters.forecastformat;

import com.geekbrains.myweather.model.AppSettings;
import com.geekbrains.myweather.model.ConverterDate;
import com.geekbrains.myweather.rest.model.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

public class ForecastTomorrow implements ForecastFormat {
    @Override
    public List<WeatherInfo> format(List<WeatherInfo> fullForecast) {
        if(fullForecast == null || fullForecast.size()<1) {
            return null;
        } else {
            List<WeatherInfo> tomorrowForecast=new ArrayList<>();
            String day= ConverterDate.extract(AppSettings.get().getToday(),"dd");
            for (int i=0;i<fullForecast.size();i++){
                String temp= ConverterDate.extract(fullForecast.get(i).date,"dd");
                if(!day.equals(temp)){
                    for(int x=i;x<i+8;x++){
                        tomorrowForecast.add(fullForecast.get(x));
                        ConverterDate.formatTitle(fullForecast.get(x),false);
                    }
                    return tomorrowForecast;
                }
            }
            return null;
        }
    }
}
