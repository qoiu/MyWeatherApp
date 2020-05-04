package com.geekbrains.myweather.ui.main;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.geekbrains.myweather.App;
import com.geekbrains.myweather.LocationDataAdapter;
import com.geekbrains.myweather.MainActivity;
import com.geekbrains.myweather.R;
import com.geekbrains.myweather.RecyclerDataAdapter;
import com.geekbrains.myweather.SettingsSingleton;
import com.geekbrains.myweather.ThermometerView;
import com.geekbrains.myweather.Weather;
import com.geekbrains.myweather.WeatherHelper;
import com.geekbrains.myweather.rest.OpenWeatherRepo;
import com.geekbrains.myweather.rest.dao.WeatherDao;
import com.geekbrains.myweather.rest.entities.WeatherListArray;
import com.geekbrains.myweather.rest.entities.WeatherRequestRestModel;
import com.geekbrains.myweather.rest.model.WeatherInfo;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainFragment extends Fragment {
    private final float WIND_ALERT = 15f;
    private TextView cityName, cityTemperature;
    private TextView itemPressure, itemHumidity, itemWind, itemWindDirection;
    private TextView itemHintPressure, itemHintHumidity, itemHintWind;
    private ImageView imageMain;
    private RecyclerView recyclerView;
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
                .getWeatherDao();
        weatherHelper = new WeatherHelper(weatherDao);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setView(view);
        initDatabase();
        defaultPrefs = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        Weather.setWindDirection(view);
        fillViewFromGeoLocation();
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
    }

    private void fillViewFromGeoLocation() {
        Location location = SettingsSingleton.getInstance().getLocation();
        String city = SettingsSingleton.getInstance().getCityName();
        if (city.equals("")) {
            fillViewFromDefPref();
            if (location != null) {
                updateWeatherData(location);
            }else{
                updateWeatherData(city);
            }
        }else {
            if (location != null) {
                fillCityInfoFromDao(LocationDataAdapter.getCityByLoc(location));
                updateWeatherData(location);
            }else{
                fillViewFromDefPref();
            }
        }
    }

    private void fillCityInfoFromDao(String city) {
        WeatherInfo weather = App.getInstance().getWeatherDao().getWeather(city);
        cityName.setText(city);
        cityTemperature.setText(weather.getTemperature());
        thermometerView.setTemperature(weather.temperature);
        savePreference(defaultPrefs, weather);
        Picasso.get()
                .load(Weather.getImgUrlFromString(weather.clouds))
                .placeholder(R.mipmap.weather_clear)
                .into(imageMain);
    }

    private void fillViewFromDefPref() {
        SettingsSingleton.getInstance().setCityName(defaultPrefs.getString("name", ""));
        if (defaultPrefs.getString("name", "null").equals("null")) {
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
                            weatherHelper.addCityWeather(response.body(), city);
                            fillCityInfoFromDao(city);
                            foregroundWindAlert(response.body());
                            Location loc = new Location("fromCity");
                            loc.setLatitude(response.body().cityId.cordRestModel.lat);
                            loc.setLongitude(response.body().cityId.cordRestModel.lon);
                            SettingsSingleton.getInstance().setLocation(loc);
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

    private void updateWeatherData(final Location loc) {
        OpenWeatherRepo.getSingleton().getAPI().loadWeatherFromGeo(loc.getLatitude(), loc.getLongitude(),
                "f3b8d2a726a6a983d8606e27c29b9566", "metric")
                .enqueue(new Callback<WeatherRequestRestModel>() {
                    @Override
                    public void onResponse(@NonNull Call<WeatherRequestRestModel> call,
                                           @NonNull Response<WeatherRequestRestModel> response) {
                        if (response.body() != null && response.isSuccessful()) {
                            String city = response.body().cityId.name;
                            if (city == null) {
                                alertWrongCity("City");
                                return;
                            }
                            weatherHelper.addCityWeather(response.body(), city);
                            fillCityInfoFromDao(city);

                            SettingsSingleton.getInstance().setLocation(loc);
                            foregroundWindAlert(response.body());
                            setRecyclerView(response.body());
                        } else {
                            if (response.code() == 404) {
                                alertWrongCity(LocationDataAdapter.getCityByLoc(loc));
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

    private void foregroundWindAlert(WeatherRequestRestModel body) {
        int ID = 1000;
        final String sep = " ";
        Map<String, Float> maxWind = new HashMap<>();
        for (WeatherListArray weather : body.listArray) {
            if (weather.windRestModel.speed > WIND_ALERT) {
                if (maxWind.get(weather.getDate()) == null) {
                    maxWind.put(weather.getDate(), weather.windRestModel.speed);
                } else {
                    maxWind.put(weather.getDate(), Math.max(maxWind.get(weather.getDate()), weather.windRestModel.speed));
                }
            }
        }
        for (Map.Entry<String, Float> wind : maxWind.entrySet()) {
            StringBuilder msg = new StringBuilder()
                    .append(wind.getKey()).append(sep)
                    .append("ожидается сильный ветер до").append(sep)
                    .append(String.format(Locale.getDefault(), "%.1f", wind.getValue())).append("\n");
            NotificationCompat.Builder builder = new NotificationCompat.Builder(requireActivity(), "2")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Warning:")
                    .setContentText(msg.toString());
            NotificationManager notificationManager =
                    (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.notify(ID++, builder.build());
        }
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

    private void savePreference(SharedPreferences sharedPreferences, WeatherInfo weather) {
        String[] keys = {"name", "temperatureString", "sky"};
        String[] values = {
                weather.cityName,
                weather.getTemperature(),
                weather.clouds
        };
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(keys[0], values[0]);
        editor.putString(keys[1], values[1]);
        editor.putString(keys[2], values[2]);
        editor.putFloat("temperatureFloat", weather.temperature);
        editor.apply();
    }
}
