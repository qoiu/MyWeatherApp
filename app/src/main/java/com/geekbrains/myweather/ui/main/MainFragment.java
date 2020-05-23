package com.geekbrains.myweather.ui.main;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.myweather.MainActivity;
import com.geekbrains.myweather.MainInformationData;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.ui.recyclers.RecyclerDataAdapter;
import com.geekbrains.myweather.AppSettings;
import com.geekbrains.myweather.ThermometerView;
import com.geekbrains.myweather.Weather;
import com.geekbrains.myweather.presenters.MainFragmentPresenter;
import com.squareup.picasso.Picasso;
import java.util.Locale;

public class MainFragment extends Fragment implements MainFragmentInterface {
    private TextView cityName, cityTemperature;
    private TextView itemPressure, itemHumidity, itemWind, itemWindDirection;
    private TextView itemHintPressure, itemHintHumidity, itemHintWind;
    private ProgressBar progressBar;
    private ImageView imageMain;
    private RecyclerView recyclerView;
    private ThermometerView thermometerView;
    private SharedPreferences defaultPrefs;
    private int notify_id = 1000;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView(view);
        defaultPrefs = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if(AppSettings.get().getCityName().equals(""))
        fillViewFromDefPref();
        setRecyclerView();
        Weather.setWindDirection(view);
        MainFragmentPresenter.get().bindView(this);
        MainFragmentPresenter.get().onCreate();
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
        thermometerView = view.findViewById(R.id.mainThermometerView);
        progressBar=view.findViewById(R.id.progressBar);
    }

    private void setRecyclerView() {
        RecyclerDataAdapter recyclerDataAdapter = new RecyclerDataAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerDataAdapter);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        MainFragmentPresenter.get().firstRun();
        applySettings();
    }

    private void applySettings() {
        int humidity = !AppSettings.get().isSettingHumidity() ? View.GONE : View.VISIBLE;
        int pressure = !AppSettings.get().isSettingPressure() ? View.GONE : View.VISIBLE;
        int wind = !AppSettings.get().isSettingWnd() ? View.GONE : View.VISIBLE;
        itemHumidity.setVisibility(humidity);
        itemHintHumidity.setVisibility(humidity);
        itemWind.setVisibility(wind);
        itemHintWind.setVisibility(wind);
        itemWindDirection.setVisibility(wind);
        itemPressure.setVisibility(pressure);
        itemHintPressure.setVisibility(pressure);
    }

    private void fillViewFromDefPref() {
        MainInformationData informationData=new MainInformationData();
        String city = defaultPrefs.getString("name", "Moscow");
        AppSettings.get().setCityName(city);
        informationData.setCityNameValue(city);
        informationData.setTemperature(defaultPrefs.getString("temperatureString", "0"));
        informationData.setImgUrl(defaultPrefs.getString("sky", Weather.getImgUrlFromString("CLEAR")));
        informationData.setTemperatureValue(defaultPrefs.getFloat("temperatureFloat", 0));
        setMainMenu(informationData);
    }

    @Override
    public void setMainMenu(MainInformationData informationData) {
        cityName.setText(informationData.getCityNameValue());
        cityTemperature.setText(informationData.getTemperature());
        thermometerView.setTemperature(informationData.getTemperatureValue());
        Picasso.get()
                .load(informationData.getImgUrl())
                .placeholder(R.mipmap.weather_clear)
                .into(imageMain);
        savePreference(informationData);
    }

    @Override
    public void showAlert(String title, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(title)
                .setMessage(msg)
                .setIcon(R.mipmap.ic_launcher_round)
                .setPositiveButton(R.string.connection_error_btn,
                        (dialog, id) -> MainActivity.navigate(R.id.nav_cities));
        builder.create();
        builder.show();
    }

    @Override
    public void notifyWindAlert(String city, String date, float wind) {
        String msg = String.format(Locale.getDefault(),
                getResources().getString(R.string.strong_wind_warning),date, wind);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireActivity(), "2")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(city+": ")
                .setContentText(msg);
        NotificationManager notificationManager =
                (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(notify_id++, builder.build());
    }

    @Override
    public void updateData() {
        progressBar.setVisibility(View.GONE);
        setRecyclerView();
    }

    @Override
    public void savePreference(MainInformationData informationData) {
        String[] keys = {"name", "temperatureString", "sky"};
        String[] values = {
                informationData.getCityNameValue(),
                informationData.getTemperature(),
                informationData.getImgUrl()
        };
        SharedPreferences.Editor editor = defaultPrefs.edit();
        for(int i=0;i<keys.length;i++){
            editor.putString(keys[i], values[i]);
        }
        editor.putFloat("temperatureFloat", informationData.getTemperatureValue());
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        MainFragmentPresenter.get().unbindView();
    }
}
