package com.geekbrains.myweather.ui.main;

import android.app.AlertDialog;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.myweather.App;
import com.geekbrains.myweather.MainActivity;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.RecyclerDataAdapter;
import com.geekbrains.myweather.SettingsSingleton;
import com.geekbrains.myweather.ThermometerView;
import com.geekbrains.myweather.Weather;
import com.geekbrains.myweather.WeatherHelper;
import com.geekbrains.myweather.rest.OpenWeatherRepo;
import com.geekbrains.myweather.rest.dao.WeatherDao;
import com.geekbrains.myweather.rest.entities.WeatherRequestRestModel;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {

    private TextView cityName, cityTemperature;
    private TextView itemPressure, itemHumidity, itemWind, itemWindDirection;
    private TextView itemHintPressure, itemHintHumidity, itemHintWind;
    private ImageView imageMain;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ThermometerView thermometerView;
    private SharedPreferences defaultPrefs;
    private WeatherHelper weatherHelper;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    private void initDatabase() {
        WeatherDao weatherDao = App
                .getInstance()
                .getEducationDao();
        weatherHelper = new WeatherHelper(weatherDao);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView(view);
        initDatabase();
        defaultPrefs = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        if (SettingsSingleton.getInstance().getCityName().equals("")) fillView();
        Weather.setWindDirection(view);
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
        thermometerView = view.findViewById(R.id.mainThermometerView);
    }

    private void fillView() {
        SettingsSingleton.getInstance().setCityName(defaultPrefs.getString("name", ""));
        if(defaultPrefs.getString("name","null").equals("null")){
            updateWeatherData("Moscow");
        }
        cityName.setText(defaultPrefs.getString("name", "Moscow"));
        cityTemperature.setText(defaultPrefs.getString("temperatureString", "0"));
        thermometerView.setTemperature(defaultPrefs.getFloat("temperatureFloat", 0));
        Picasso.get()
                .load(Weather.getImgUrlFromString(defaultPrefs.getString("sky", "")))
                .placeholder(R.mipmap.weather_clear)
                .into(imageMain);
    }

    private void setRecyclerView(WeatherRequestRestModel body) {
        RecyclerDataAdapter adapter = new RecyclerDataAdapter(body);
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

    private void updateWeatherData(final String city) {
        OpenWeatherRepo.getSingleton().getAPI().loadWeather(city.toLowerCase(),
                "f3b8d2a726a6a983d8606e27c29b9566", "metric")
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            weatherHelper.addCityWeather(response.body());
                            fillCityInfo(response.body());
                            setRecyclerView(response.body());
                        } else {
                            if (response.code() == 404) {
                                alertWrongCity(city);
                            } else if (response.code() == 401) {
                                alertAuth();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<WeatherRequestRestModel> call, Throwable t) {
                        alertConnection();
                    }
                });
    }

    private void fillCityInfo(WeatherRequestRestModel body) {
        cityName.setText(body.cityId.name);
        MainActivity.addCityName(body.cityId.name);
        cityTemperature.setText(String.valueOf(body.listArray[0].weatherMainData.getTemperature()));
        thermometerView.setTemperature(body.listArray[0].weatherMainData.temperature);
        savePreference(defaultPrefs, body);
        Picasso.get()
                .load(Weather.getImgUrlFromString(body.listArray[0].weatherExtraData[0].main))
                .placeholder(R.mipmap.weather_clear)
                .into(imageMain);
    }

    private void alertWrongCity(String city) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(city + getResources().getString(R.string.connection_city_wrong_title))
                .setMessage(R.string.connection_city_wrong_msg)
                .setIcon(R.mipmap.ic_launcher_round)
                .setPositiveButton(R.string.okay, (dialog, which) -> MainActivity.navigate(R.id.nav_cities))
                .setCancelable(false);
        builder.create();
        builder.show();
    }

    private void alertAuth() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.connection_auth_title)
                .setMessage(R.string.connection_auth_msg)
                .setIcon(R.mipmap.ic_launcher_round)
                .setPositiveButton(R.string.okay, (dialog, which) -> requireActivity().finish())
                .setCancelable(false);
        builder.create();
        builder.show();
    }

    private void alertConnection() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.connection_error_title)
                .setMessage(R.string.connection_error_msg)
                .setIcon(R.mipmap.ic_launcher_round)
                .setPositiveButton(R.string.connection_error_btn,
                        (dialog, id) -> MainActivity.navigate(R.id.nav_cities));
        builder.create();
        builder.show();
    }

    private void applySettings() {
        updateWeatherData(SettingsSingleton.getInstance().getCityName());
        int humidity = !SettingsSingleton.getInstance().isSettingHumidity() ? View.GONE : View.VISIBLE;
        int pressure = !SettingsSingleton.getInstance().isSettingPressure() ? View.GONE : View.VISIBLE;
        int wind = !SettingsSingleton.getInstance().isSettingWnd() ? View.GONE : View.VISIBLE;
        itemHumidity.setVisibility(humidity);
        itemHintHumidity.setVisibility(humidity);
        itemWind.setVisibility(wind);
        itemHintWind.setVisibility(wind);
        itemWindDirection.setVisibility(wind);
        itemPressure.setVisibility(pressure);
        itemHintPressure.setVisibility(pressure);
    }

    private void savePreference(SharedPreferences sharedPreferences, WeatherRequestRestModel body) {
        String[] keys = {"name", "temperatureString", "sky"};
        String[] values = {
                body.cityId.name,
                body.listArray[0].weatherMainData.getTemperature(),
                body.listArray[0].weatherExtraData[0].main
        };
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keys[0], values[0]);
        editor.putString(keys[1], values[1]);
        editor.putString(keys[2], values[2]);
        editor.putFloat("temperatureFloat", body.listArray[0].weatherMainData.temperature);
        editor.apply();
    }
}
