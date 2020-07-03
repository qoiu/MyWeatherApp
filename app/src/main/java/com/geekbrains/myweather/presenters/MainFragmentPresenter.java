package com.geekbrains.myweather.presenters;


import androidx.annotation.NonNull;

import com.geekbrains.myweather.AppComponent;
import com.geekbrains.myweather.DaggerAppComponent;
import com.geekbrains.myweather.model.AppSettings;
import com.geekbrains.myweather.model.Model;
import com.geekbrains.myweather.presenters.forecastformat.ForecastFormat;
import com.geekbrains.myweather.presenters.forecastformat.ForecastToday;
import com.geekbrains.myweather.rest.model.WeatherInfo;
import com.geekbrains.myweather.ui.main.MainFragmentInterface;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class MainFragmentPresenter extends BasePresenter<MainFragmentInterface> {

    private static MainFragmentPresenter presenter;
    //private Model model;
    private boolean cheatState = false;
    private final float WIND_ALERT = 15.0f;
    private MainFragmentInterface view;
    private ForecastFormat forecast;
    @Inject
    Model model;

    public static MainFragmentPresenter get() {
        if (presenter == null) presenter = new MainFragmentPresenter();
        return presenter;

    }

    public MainFragmentPresenter() {
        AppComponent component= DaggerAppComponent.create();
        component.inject(this);
        //this.model = App.getModel();
        model.subscribeForUpdate(observer);
        forecast = new ForecastToday();
    }

    private void createObserver() {
        view.setReactForecast(new SingleObserver<ForecastFormat>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ForecastFormat forecastFormat) {
                forecast = forecastFormat;
                updateView();
            }

            @Override
            public void onError(Throwable e) {

            }
        });

    }

    private Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {
            switch (integer) {
                case 200:
                    updateView();
                    foregroundWindAlert();
                    break;
                case 401:
                    sendAuthProblems();
                    break;
                case 404:
                    sendCityNotFoundErr(AppSettings.get().getCityName());
                    break;
            }
        }

        @Override
        public void onError(Throwable e) {
            sendConnectionError("");
        }

        @Override
        public void onComplete() {

        }
    };

    private void updateView() {
        view.setMainMenu(model.loadData());
        List<WeatherInfo> formated = forecast.format(model.getUpdatedListWeatherInfo());
        if (formated != null) view.updateData(formated);
    }

    @Override
    public void bindView(@NonNull MainFragmentInterface view) {
        super.bindView(view);
        this.view = view;
        createObserver();
        updateView();
        model.updateData();
    }

    public void showCheat() {
        cheatState = !cheatState;
        view.showCheat(cheatState);
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

    private void sendConnectionError(String err) {
        String title = "Connection problems";
        String msg = "Please check your internet connection.\n" + err;
        view().showAlert(title, msg);
    }

    private void foregroundWindAlert() {
        Map<String, Float> maxWind = model.getWindAlert(WIND_ALERT);
        for (Map.Entry<String, Float> wind : maxWind.entrySet()) {
            view().notifyWindAlert(AppSettings.get().getCityName(), wind.getKey(), wind.getValue());
        }
    }
}
