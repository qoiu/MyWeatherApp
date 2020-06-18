package com.geekbrains.myweather.presenters;



import androidx.annotation.NonNull;

import com.geekbrains.myweather.AppSettings;
import com.geekbrains.myweather.Model;
import com.geekbrains.myweather.ui.main.MainFragmentInterface;

import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainFragmentPresenter extends BasePresenter<MainFragmentInterface> {

    private static MainFragmentPresenter presenter;
    private Model model;
    private final float WIND_ALERT = 15.0f;
    private MainFragmentInterface view;

    public static MainFragmentPresenter get() {
        if (presenter == null) presenter = new MainFragmentPresenter();
        return presenter;
    }

    public MainFragmentPresenter() {
        this.model = new Model();
        model.subscribeForUpdate(observer);
    }

    private Observer<Integer> observer = new Observer<Integer>() {
        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onNext(Integer integer) {
            switch (integer) {
                case 200:
                    view.setMainMenu(model.loadData());
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

    @Override
    public void bindView(@NonNull MainFragmentInterface view) {
        super.bindView(view);
        this.view=view;
        view.setMainMenu(model.loadData());
        model.updateData();
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
        String msg = "Please check your internet connection.\n"+err;
        view().showAlert(title, msg);
    }

    private void foregroundWindAlert() {
        Map<String, Float> maxWind = model.getWindAlert(WIND_ALERT);
        for (Map.Entry<String, Float> wind : maxWind.entrySet()) {
            view().notifyWindAlert(AppSettings.get().getCityName(), wind.getKey(), wind.getValue());
        }
    }
}
