package com.geekbrains.myweather.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.myweather.CityData;
import com.geekbrains.myweather.CityList;
import com.geekbrains.myweather.MainActivity;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.RecyclerDataAdapter;
import com.geekbrains.myweather.Singleton;
import com.geekbrains.myweather.ThermometerView;
import com.geekbrains.myweather.Weather;

public class MainFragment extends Fragment {

    private TextView cityName, cityTemperature;
    private TextView itemPressure, itemHumidity, itemWind, itemWindDirection;
    private TextView itemHintPressure, itemHintHumidity, itemHintWind, cityNotFound;
    private ImageView imageMain;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Handler handler = new Handler();
    private CityData currentCity;
    private ThermometerView thermometerView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView(view);
        Weather.setView(view);
    }

    private void setView(View view) {
        itemHintHumidity = view.findViewById(R.id.itemMainDayInfoHintHumidity);
        itemHintPressure = view.findViewById(R.id.itemMainDayInfoHintPressure);
        itemHintWind = view.findViewById(R.id.itemMainDayInfoHintWind);
        itemHumidity = view.findViewById(R.id.itemMainDayInfoHumidity);
        itemWind = view.findViewById(R.id.itemMainDayInfoWind);
        itemWindDirection = view.findViewById(R.id.itemMainDayInfoWindDirrection);
        itemPressure = view.findViewById(R.id.itemMainDayInfoPressure);
        cityTemperature = view.findViewById(R.id.textTemperature);
        cityName = view.findViewById(R.id.textCityName);
        imageMain = view.findViewById(R.id.imageMain);
        recyclerView = view.findViewById(R.id.recyclerViewMain);
        progressBar = view.findViewById(R.id.progressBarMain);
        cityNotFound = view.findViewById(R.id.mainCityNotFound);
        thermometerView = view.findViewById(R.id.mainThermometerView);
    }

    private void applySettings() {
        int humidity = !Singleton.getInstance().isSettingHumidity() ? View.GONE : View.VISIBLE;
        int pressure = !Singleton.getInstance().isSettingPressure() ? View.GONE : View.VISIBLE;
        int wind = !Singleton.getInstance().isSettingWnd() ? View.GONE : View.VISIBLE;
        itemHumidity.setVisibility(humidity);
        itemHintHumidity.setVisibility(humidity);
        itemWind.setVisibility(wind);
        itemHintWind.setVisibility(wind);
        itemWindDirection.setVisibility(wind);
        itemPressure.setVisibility(pressure);
        itemHintPressure.setVisibility(pressure);
        cityName.setText(Singleton.getInstance().getCityName());
        currentCity = CityList.getCity(Singleton.getInstance().getCityName());
        cityNotFound.setVisibility(View.GONE);
        if (currentCity != null && currentCity != CityList.CITY_NOT_FOUND) {
            fillCityInfo();
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void fillCityInfo() {
        if (currentCity != CityList.CITY_NOT_FOUND) {
            cityName.setText(currentCity.getCityName());
            cityTemperature.setText(String.valueOf(currentCity.getTodayInfo().getFormatedTemperature()));
            thermometerView.setTemperature(currentCity.getTodayInfo().getTemperature());
            imageMain.setImageResource(Weather.getImgFromString(currentCity.getTodayInfo().getIcoDescription().toUpperCase()));
            setRecyclerView();
        }
    }

    private void setRecyclerView() {
        RecyclerDataAdapter adapter = new RecyclerDataAdapter(currentCity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        applySettings();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(dataLoadedReceiver, new IntentFilter(MainActivity.BROADCAST_ACTION_CITY_LOADED));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(dataLoadedReceiver);
    }

    private BroadcastReceiver dataLoadedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            currentCity = CityList.getCity(Singleton.getInstance().getCityName());
            progressBar.setVisibility(View.GONE);
            if (currentCity == CityList.CITY_NOT_FOUND || currentCity == null) {
                MainActivity.showAlert();
            } else {
                fillCityInfo();
            }
        }
    };
}
