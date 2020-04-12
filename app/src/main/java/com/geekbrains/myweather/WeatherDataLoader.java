package com.geekbrains.myweather;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Locale;

public class WeatherDataLoader {
    private static final String OPEN_WEATHER_APP_KEY = "f3b8d2a726a6a983d8606e27c29b9566";
    private static final String OPEN_WEATHER_API_URL =
            "https://api.openweathermap.org/data/2.5/forecast?q=%s&units=metric&mode=json";
    private static final String KEY = "x-api-key";
    private static final Handler handler = new Handler();

    public static void addCityFromWeatherApi(final String cityName) {
        Log.w("WeatherDataLoader","Trying to add "+cityName);
        if (CityList.getCity(cityName) == null) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    final JSONObject jsonObject = getJSONData(cityName);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (jsonObject != null) {
                                CityList.addCity(fillWeather(jsonObject, cityName));
                            }
                        }
                    });
                }
            };
            t.start();
        }
    }

        private static JSONObject getJSONData(String city){
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
                Log.v("WeatherDataLoader",rawData.toString());
                if (jsonObject.getInt("cod") != 200) {
                    return null;
                } else {
                    return jsonObject;
                }
            } catch (IOException | JSONException e) {
                Log.w("WeatherDataLoader",e);
                e.printStackTrace();
                return null;
            }
        }

        private static CityData fillWeather (JSONObject jsonObject, String cityName){
            if (jsonObject != null) {
                CityData city = new CityData(cityName);
                try {
                    JSONArray jsonArray = jsonObject.getJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        WeatherInfo weather = new WeatherInfo();
                        try {
                            weather.setDate(new Date(jsonArray.getJSONObject(i).getLong("dt") * 1000));
                            weather.setTemperature(jsonArray.getJSONObject(i).getJSONObject("main")
                                    .getDouble("temp"));
                            weather.setIcoDescription(jsonArray.getJSONObject(i).getJSONArray("weather")
                                    .getJSONObject(0).getString("main"));
                            weather.setHumidity(jsonArray.getJSONObject(i).getJSONObject("main")
                                    .getInt("humidity"));
                            weather.setPressure(jsonArray.getJSONObject(i).getJSONObject("main")
                                    .getInt("pressure"));
                            weather.setWindDirection(jsonArray.getJSONObject(i)
                                    .getJSONObject("wind").getInt("deg"));
                            weather.setWindSpeed(jsonArray.getJSONObject(i)
                                    .getJSONObject("wind").getDouble("speed"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        String st = weather.getHour();
                        if (city.getTodayInfo() == null) {
                            if (weather.dateLessThanNow()) {
                                city.setTodayInfo(weather);
                            }
                        }
                        if (st.equals("12")) {
                            city.addWeather(weather);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return city;
            }
            return null;
        }

    }
