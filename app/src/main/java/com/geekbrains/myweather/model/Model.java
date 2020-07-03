package com.geekbrains.myweather.model;


import android.location.Location;
import android.util.Log;

import com.geekbrains.myweather.App;
import com.geekbrains.myweather.LocationModule;
import com.geekbrains.myweather.MainInformationData;
import com.geekbrains.myweather.Weather;
import com.geekbrains.myweather.WeatherHelper;
import com.geekbrains.myweather.rest.OpenWeatherRepo;
import com.geekbrains.myweather.rest.dao.WeatherDao;
import com.geekbrains.myweather.rest.entities.WeatherRequestRestModel;
import com.geekbrains.myweather.rest.model.WeatherInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@Module
public class Model {

    private WeatherHelper weatherHelper;

    public Model() {
        initDatabase();
    }

    @Provides
    @Singleton
    public Model getModel() {
        return this;
    }

    List<Observer> observerList = new ArrayList<>();


    private void initDatabase() {
        WeatherDao weatherDao = App
                .getInstance()
                .getWeatherDao();
        weatherHelper = new WeatherHelper(weatherDao);
        firstLoad();
    }

    private void firstLoad() {
        Location location = AppSettings.get().getLocation();
        String city = AppSettings.get().getCityName();
        loadData();
        if (location != null) {
            updateWeatherData(location);
            return;
        }
        if (!city.equals("")) {
            updateWeatherData(city);
        }
    }

    public void updateData() {
        Location location = AppSettings.get().getLocation();
        String city = AppSettings.get().getCityName();
        loadData();
        if (location != null) {
            updateWeatherData(location);
            return;
        }
        if (!city.equals("")) {
            updateWeatherData(city);
        }
    }

    @Provides
    public MainInformationData loadData() {
        String city = AppSettings.get().getCityName();
        long date = AppSettings.get().getToday();
        WeatherInfo weather = App.getInstance().getWeatherDao().getCity(city, date);
        if (weather == null) {
            return fillFromSingleton();
        }
        MainInformationData informationData = new MainInformationData();
        informationData.setCityNameValue(city);
        informationData.setImgUrl(Weather.getImgUrlFromString(weather.clouds));
        Log.w("Weather", ConverterDate.extract(date, "dd.MM") + " " + weather.clouds);
        informationData.setTemperature(weather.getTemperatureString());
        informationData.setTemperatureValue(weather.temperature);
        return informationData;
    }

    @Provides
    public MainInformationData fillFromSingleton() {
        MainInformationData informationData = new MainInformationData();
        informationData.setCityNameValue(AppSettings.get().getCityName());
        informationData.setTemperatureValue(0);
        informationData.setTemperature("0");
        informationData.setImgUrl(Weather.getImgUrlFromString("Clear"));
        return informationData;
    }

    public void updateWeatherData(final Location loc) {
        Observable<Integer> observable = OpenWeatherRepo.getSingleton().getAPI().loadWeatherFromGeo(loc.getLatitude(), loc.getLongitude(),
                "f3b8d2a726a6a983d8606e27c29b9566", "metric",
                AppSettings.get().getLocalization()).map(response -> {
            String city = LocationModule.getInstance().getCityByLoc(loc);
            dataUpdater(response, city);
            return response.code();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
        for (Observer observer : observerList) {
            observable.subscribe(observer);
        }
    }

    public void updateWeatherData(final String city) {
        Observable<Integer> observable = OpenWeatherRepo.getSingleton().getAPI().loadWeather(city.toLowerCase(),
                "f3b8d2a726a6a983d8606e27c29b9566", "metric",
                AppSettings.get().getLocalization()).map(response -> {
            dataUpdater(response, city);
            return response.code();
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());

        for (Observer observer : observerList) {
            observable.subscribe(observer);
        }
    }

    private void dataUpdater(@NonNull Response<WeatherRequestRestModel> response, String city) {
        Log.w("Download result", String.valueOf(response.code()));
        if (response.body() != null && response.isSuccessful()) {
            weatherHelper.addCityWeather(response.body());
            loadData();
        }
    }

    public void subscribeForUpdate(Observer observer) {
        observerList.add(observer);
    }

    @Provides
    public Map<String, Float> getWindAlert(float windAlert) {
        Map<String, Float> maxWind = new HashMap<>();
        List<WeatherInfo> forecast = App.getInstance().getWeatherDao().getForecast(AppSettings.get().getCityName());
        for (WeatherInfo weather : forecast) {
            if (weather.windSpeed > windAlert) {
                String date = ConverterDate.extract(weather.date, "dd.MM");
                if (maxWind.get(date) == null) {
                    maxWind.put(date, weather.windSpeed);
                } else {
                    maxWind.put(date, Math.max(maxWind.get(date), weather.windSpeed));
                }
            }
        }
        return maxWind;
    }

    @Provides
    public List<WeatherInfo> getUpdatedListWeatherInfo() {
        return App.getInstance().getWeatherDao().getForecastFromNow(AppSettings.get().getCityName(),
                ConverterDate.getDefineHour(AppSettings.get().getToday(), 0));
    }

    @Provides
    public List<WeatherInfo> getUpdatedListWeatherInfoTest() {
        return App.getInstance().getWeatherDao().getForecastTest(AppSettings.get().getCityName());
    }
}
