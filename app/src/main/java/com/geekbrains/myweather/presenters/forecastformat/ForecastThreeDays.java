package com.geekbrains.myweather.presenters.forecastformat;

import com.geekbrains.myweather.model.ConverterDate;
import com.geekbrains.myweather.rest.model.WeatherInfo;

import java.util.ArrayList;
import java.util.List;

public class ForecastThreeDays implements ForecastFormat {
    @Override
    public List<WeatherInfo> format(List<WeatherInfo> fullForecast) {
        if (fullForecast == null || fullForecast.size() < 1) {
            return null;
        } else {
            List<WeatherInfo> tomorrowForecast = new ArrayList<>();
            String lastDay = ConverterDate.extract(fullForecast.get(0).date + 324000,"dd");
            for (int i = 0; i < fullForecast.size(); i++) {
                if (ConverterDate.extract(fullForecast.get(i).date,"dd").equals(lastDay))
                    return tomorrowForecast;
                if (isValid(ConverterDate.extract(fullForecast.get(i).date,"HH"))) {
                    tomorrowForecast.add(fullForecast.get(i));
                    ConverterDate.formatTitle(tomorrowForecast.get(tomorrowForecast.size() - 1), i == 0);
                }
            }
            return tomorrowForecast;
        }
    }

    private boolean isValid(String hour) {
        return (hour.equals("00") || hour.equals("06") || hour.equals("12") || hour.equals("18"));
    }
}
