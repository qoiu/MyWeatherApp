package com.geekbrains.myweather.presenters;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;

import com.geekbrains.myweather.App;
import com.geekbrains.myweather.Converter;
import com.geekbrains.myweather.LocationModule;
import com.geekbrains.myweather.AppSettings;
import com.geekbrains.myweather.MainInformationData;
import com.geekbrains.myweather.Weather;
import com.geekbrains.myweather.WeatherHelper;
import com.geekbrains.myweather.rest.OpenWeatherRepo;
import com.geekbrains.myweather.rest.dao.WeatherDao;
import com.geekbrains.myweather.rest.entities.WeatherListArray;
import com.geekbrains.myweather.rest.entities.WeatherRequestRestModel;
import com.geekbrains.myweather.rest.model.WeatherInfo;
import com.geekbrains.myweather.ui.main.MainFragmentInterface;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragmentPresenter extends BasePresenter<MainFragmentInterface> {

    private static MainFragmentPresenter presenter;
    private WeatherHelper weatherHelper;
    private final float WIND_ALERT = 15.0f;

    public void onCreate() {
        initDatabase();
    }

    public static MainFragmentPresenter get(){
        if(presenter==null)presenter=new MainFragmentPresenter();
        return presenter;
    }


    private void initDatabase() {
        WeatherDao weatherDao = App
                .getInstance()
                .getWeatherDao();
        weatherHelper = new WeatherHelper(weatherDao);
    }

    public void firstRun() {
        Location location = AppSettings.get().getLocation();
        String city = AppSettings.get().getCityName();
        tryToFillCityInfoFromDao();
        if (location != null) {
            updateWeatherData(location);
            return;
        }
        if (!city.equals("")) {
            updateWeatherData(city);
        }
    }

    private void tryToFillCityInfoFromDao() {
        String city = AppSettings.get().getCityName();
        long date = AppSettings.get().getToday();
        WeatherInfo weather = App.getInstance().getWeatherDao().getCity(city, date);
        if (weather == null) {
            fillFromSingleton();

            return;
        }
        MainInformationData informationData = new MainInformationData();
        informationData.setCityNameValue(city);
        informationData.setImgUrl(Weather.getImgUrlFromString(weather.clouds));
        Log.w("Weather",Converter.convertDateToString(date)+" "+weather.clouds);
        informationData.setTemperature(weather.getTemperatureString());
        informationData.setTemperatureValue(weather.temperature);
        view().setMainMenu(informationData);
    }

    private void fillFromSingleton() {
        MainInformationData informationData = new MainInformationData();
        informationData.setCityNameValue(AppSettings.get().getCityName());
        informationData.setTemperatureValue(0);
        informationData.setTemperature("0");
        informationData.setImgUrl(Weather.getImgUrlFromString("Clear"));
        view().setMainMenu(informationData);
    }

    private void updateWeatherData(final Location loc) {
        OpenWeatherRepo.getSingleton().getAPI().loadWeatherFromGeo(loc.getLatitude(), loc.getLongitude(),
                "f3b8d2a726a6a983d8606e27c29b9566", "metric",
                AppSettings.get().getLocalization())
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        String city = LocationModule.getInstance().getCityByLoc(loc);
                        dataUpdater(response, city);
                    }

                    @Override
                    public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
                        sendConnectionError();
                    }
                });
    }

    private void updateWeatherData(final String city) {
        OpenWeatherRepo.getSingleton().getAPI().loadWeather(city.toLowerCase(),
                "f3b8d2a726a6a983d8606e27c29b9566", "metric",
                AppSettings.get().getLocalization())
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        dataUpdater(response, city);
                    }

                    @Override
                    public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
                        sendConnectionError();
                    }
                });
    }

    private void dataUpdater(@NonNull Response<WeatherRequestRestModel> response, String city) {
        Log.w("Downoad result",String.valueOf(response.code()));
        if (response.body() != null && response.isSuccessful()) {
            weatherHelper.addCityWeather(response.body(), city);
            tryToFillCityInfoFromDao();
            foregroundWindAlert(response.body());
            view().updateData();
        } else {
            if (response.code() == 404) {
                sendCityNotFoundErr(city);
            } else if (response.code() == 401) {
                sendAuthProblems();
            }
        }
    }

    private void sendAuthProblems() {
        String title = "Connection problems";
        String msg = "Problems with service authorization";
        view().showAlert(title, msg);
    }

    private void sendCityNotFoundErr(String city) {
        String title = city + "not found";
        String msg = "Please choose correct city";
        view().showAlert(title, msg);
    }

    private void sendConnectionError() {
        String title = "Connection problems";
        String msg = "Please check your internet connection.";
        view().showAlert(title, msg);
    }

    private void foregroundWindAlert(WeatherRequestRestModel body) {
        Map<String, Float> maxWind = new HashMap<>();
        for (WeatherListArray weather : body.listArray) {
            if (weather.windRestModel.speed > WIND_ALERT) {
                String date=Converter.convertDateToString(weather.dt);
                if (maxWind.get(date) == null) {
                    maxWind.put(date, weather.windRestModel.speed);
                } else {
                    maxWind.put(date, Math.max(maxWind.get(date), weather.windRestModel.speed));
                }
            }
        }
        for (Map.Entry<String, Float> wind : maxWind.entrySet()) {
            view().notifyWindAlert(body.cityId.name, wind.getKey(), wind.getValue());
        }
    }
}
