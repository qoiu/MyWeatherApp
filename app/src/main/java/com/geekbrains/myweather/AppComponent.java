package com.geekbrains.myweather;

import com.geekbrains.myweather.model.ConverterDate;
import com.geekbrains.myweather.model.Model;
import com.geekbrains.myweather.presenters.MainFragmentPresenter;

import dagger.Component;

@Component(modules = {Model.class, ConverterDate.class})
@javax.inject.Singleton
public interface AppComponent {
    void injectsToMainActivity(App app);
        void inject(MainFragmentPresenter presenter);
        Model getModel();
    }
