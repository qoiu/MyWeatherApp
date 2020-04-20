package com.geekbrains.myweather;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherDataService extends IntentService {
    private static final String EXTRA_CITY_NAME = "com.geekbrains.myweather.city_name";
    private static final String OPEN_WEATHER_APP_KEY = "f3b8d2a726a6a983d8606e27c29b9566";
    private static final String OPEN_WEATHER_API_URL =
            "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&mode=json";
    private static final String KEY = "x-api-key";

    public WeatherDataService() {
        super("WeatherDataService");
    }

    public static void startWeatherDataService(Context context, String city) {
        Intent intent = new Intent(context, WeatherDataService.class);
        intent.putExtra(EXTRA_CITY_NAME, city);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        addCityFromWeatherApi(intent.getStringExtra(EXTRA_CITY_NAME));
        sendBroadcastEvent();
    }

    private static void addCityFromWeatherApi(final String cityName) {
        Log.w("WeatherDataLoader", "Trying to add " + cityName);
        if (CityList.getCity(cityName) == null) {
            final JSONObject jsonObject = getJSONData(cityName);
            if (jsonObject != null) {
                CityList.addCity(WeatherDataParser.fillWeather(jsonObject));
            } else {
                CityList.addCity(cityName);
            }
        }
    }


    private static JSONObject getJSONData(String city) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_API_URL, city));
            Log.d("WeatherData", String.valueOf(url));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.addRequestProperty(KEY, OPEN_WEATHER_APP_KEY);
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder rawData = new StringBuilder(1024);
            String tempVariable;
            while ((tempVariable = reader.readLine()) != null) {
                rawData.append(tempVariable).append("\n");
            }
            reader.close();
            JSONObject jsonObject = new JSONObject(rawData.toString());
            Log.v("WeatherDataLoader", rawData.toString());
            if (jsonObject.getInt("cod") != 200) {
                return null;
            } else {
                return jsonObject;
            }
        } catch (IOException | JSONException e) {
            Log.w("WeatherDataLoader", e);
            e.printStackTrace();
            return null;
        }
    }

    private void sendBroadcastEvent() {
        Intent broadcastIntent = new Intent(MainActivity.BROADCAST_ACTION_CITY_LOADED);
        broadcastIntent.putExtra("EXTRA_RESULT", "result");
        sendBroadcast(broadcastIntent);
    }
}
