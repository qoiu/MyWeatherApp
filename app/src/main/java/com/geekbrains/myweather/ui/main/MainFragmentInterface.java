package com.geekbrains.myweather.ui.main;

import com.geekbrains.myweather.MainInformationData;
import com.geekbrains.myweather.presenters.forecastformat.ForecastFormat;
import com.geekbrains.myweather.rest.model.WeatherInfo;

import java.util.List;

import io.reactivex.SingleObserver;

public interface MainFragmentInterface {

    void setReactForecast(SingleObserver<ForecastFormat> formatObserver);

    void setMainMenu(MainInformationData informationData);

    void showAlert(String title, String msg);

    void showCheat(boolean visibility);

    void notifyWindAlert(String city, String date, float wind);

    void updateData(List<WeatherInfo> weatherInfos);

    void savePreference(MainInformationData informationData);
}
