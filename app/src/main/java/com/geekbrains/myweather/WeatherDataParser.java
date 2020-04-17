package com.geekbrains.myweather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

class WeatherDataParser {
    static CityData fillWeather(JSONObject jsonObject){
        if (jsonObject != null) {
            try {
                String cityName=jsonObject.getJSONObject("city").getString("name");
                CityData city = new CityData(cityName);
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
                return city;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
