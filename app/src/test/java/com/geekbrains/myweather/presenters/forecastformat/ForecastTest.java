package com.geekbrains.myweather.presenters.forecastformat;

import com.geekbrains.myweather.model.AppSettings;
import com.geekbrains.myweather.rest.model.WeatherInfo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ForecastTest {
    private List<WeatherInfo> forecast;
    private List<WeatherInfo> emptyList;
    private List<WeatherInfo> formattedForecastToday;
    private List<WeatherInfo> formattedForecastTomorrow;
    private List<WeatherInfo> formattedForecastTomorrowIncorrect;
    private List<WeatherInfo> formattedForecastThreeDays;
    private List<WeatherInfo> formattedForecastThreeDaysIncorrect;
    private List<WeatherInfo> formattedForecastFiveDays;
    private long[] date;

    @Before
    public void setupForecast() {
        forecast = new ArrayList<>();
        formattedForecastFiveDays = new ArrayList<>();
        formattedForecastThreeDays = new ArrayList<>();
        formattedForecastThreeDaysIncorrect = new ArrayList<>();
        formattedForecastToday = new ArrayList<>();
        formattedForecastTomorrow = new ArrayList<>();
        formattedForecastTomorrowIncorrect = new ArrayList<>();
        AppSettings.get().setToday(1593648000);
        date = new long[]{
                1593637200,
                1593648000,
                1593658800,
                1593669600,
                1593680400,
                1593691200,
                1593702000,
                1593712800,
                1593723600,
                1593734400,
                1593745200,
                1593756000,
                1593766800,
                1593777600,
                1593788400,
                1593799200,
                1593810000,
                1593820800,
                1593831600,
                1593842400,
                1593853200,
                1593864000,
                1593874800,
                1593885600,
                1593896400,
                1593907200,
                1593918000,
                1593928800,
                1593939600,
                1593950400,
                1593961200,
                1593972000,
                1593982800,
                1593993600,
                1594004400,
                1594015200,
                1594026000,
                1594036800,
                1594047600,
                1594058400};
        for (int i = 0; i < date.length; i++) {
            WeatherInfo weatherInfo = new WeatherInfo();
            weatherInfo.date = date[i];
            forecast.add(weatherInfo);
            formattedForecastTomorrow.add(weatherInfo);
            formattedForecastThreeDays.add(weatherInfo);
            formattedForecastFiveDays.add(weatherInfo);
            if (i < 3) {
                formattedForecastThreeDaysIncorrect.add(weatherInfo);
                formattedForecastTomorrowIncorrect.add(weatherInfo);
            }
        }
        formattedForecastToday = new ForecastToday().format(forecast);
        formattedForecastTomorrow = new ForecastTomorrow().format(formattedForecastTomorrow);
        formattedForecastTomorrowIncorrect = new ForecastTomorrow().format(formattedForecastTomorrowIncorrect);
        formattedForecastThreeDays = new ForecastThreeDays().format(formattedForecastThreeDays);
        formattedForecastThreeDaysIncorrect = new ForecastThreeDays().format(formattedForecastThreeDaysIncorrect);
        formattedForecastFiveDays = new ForecastFiveDays().format(formattedForecastFiveDays);
    }

    @Test
    public void countTodayLength() {
        Assert.assertEquals(8, formattedForecastToday.size());
    }

    @Test
    public void countTomorrowLength() {
        Assert.assertEquals(8, formattedForecastTomorrow.size());
    }

    @Test
    public void countThreeDaysLength() {
        Assert.assertEquals(12, formattedForecastThreeDays.size());
    }

    @Test
    public void countFiveDaysLength() {
        Assert.assertEquals(5, formattedForecastFiveDays.size());
    }

    @Test
    public void formatForecastToday() {
        for (int i = 0; i < formattedForecastToday.size(); i++) {
            Assert.assertEquals(forecast.get(i), formattedForecastToday.get(i));
        }
    }

    @Test
    public void emptyCheckToday() {
        Assert.assertNull(new ForecastToday().format(emptyList));
    }

    @Test
    public void emptyCheckTomorrow() {
        Assert.assertNull(new ForecastTomorrow().format(emptyList));
    }

    @Test
    public void emptyCheckThreeDays() {
        Assert.assertNull(new ForecastThreeDays().format(emptyList));
    }

    @Test
    public void emptyCheckFiveDays() {
        Assert.assertNull(new ForecastFiveDays().format(emptyList));
    }

    @Test
    public void nullCheckToday() {
        Assert.assertNull(new ForecastToday().format(null));
    }

    @Test
    public void nullCheckTomorrow() {
        Assert.assertNull(new ForecastTomorrow().format(null));
    }

    @Test
    public void nullCheckThreeDays() {
        Assert.assertNull(new ForecastThreeDays().format(null));
    }

    @Test
    public void nullCheckFiveDays() {
        Assert.assertNull(new ForecastFiveDays().format(null));
    }
}