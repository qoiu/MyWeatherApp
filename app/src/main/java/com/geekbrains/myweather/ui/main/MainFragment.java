package com.geekbrains.myweather.ui.main;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.myweather.CityData;
import com.geekbrains.myweather.CityList;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.RecyclerDataAdapter;
import com.geekbrains.myweather.Singleton;
import com.geekbrains.myweather.Weather;
import com.geekbrains.myweather.WeatherDataLoader;

public class MainFragment extends Fragment {

    final static String TAG = "MainActivity";
    private TextView cityName, cityTemperature;
    private TextView itemPressure, itemHumidity, itemWind, itemWindDirection;
    private TextView itemHintPressure, itemHintHumidity, itemHintWind;
    private ImageView imageMain;
    private RecyclerView recyclerView;
    ProgressBar progressBar;
    private Handler handler=new Handler();
    static final String DATA_KEY = "city_data";
    private CityData currentCity;
    private int loadedCity;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView(view);
        Weather.setView(view);
        applySettings();
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
        progressBar=view.findViewById(R.id.progressBarMain);
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
        if (currentCity == null) {
            progressBar.setVisibility(View.VISIBLE);
           waitingInternetData();
        } else {
            cityTemperature.setText(String.valueOf(currentCity.getTodayInfo().getTemperature()));
            imageMain.setImageResource(Weather.getImgFromString(currentCity.getTodayInfo().getIcoDescription().toUpperCase()));
            setRecyclerView();
        }
    }

    private void waitingInternetData(){
        new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        currentCity = CityList.getCity(Singleton.getInstance().getCityName());
                        if (currentCity == null) {
                            waitingInternetData();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            cityTemperature.setText(String.valueOf(currentCity.getTodayInfo().getTemperature()));
                            imageMain.setImageResource(Weather.getImgFromString(currentCity.getTodayInfo().getIcoDescription().toUpperCase()));
                            setRecyclerView();
                        }
                    }
                });

            }
        }.start();
    }

    private void setRecyclerView() {
        RecyclerDataAdapter adapter = new RecyclerDataAdapter(currentCity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "Stop");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        applySettings();
    }
}
