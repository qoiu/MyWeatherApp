package com.geekbrains.myweather.ui.main;

import com.geekbrains.myweather.MainInformationData;

public interface MainFragmentInterface {

    void setMainMenu(MainInformationData informationData);

    void showAlert(String title, String msg);

    void notifyWindAlert(String city, String date, float wind);

    void updateData();

    void savePreference(MainInformationData informationData);
}
