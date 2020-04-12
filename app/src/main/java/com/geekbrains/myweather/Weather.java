package com.geekbrains.myweather;

import android.view.View;

public class Weather {
    enum WeatherType {
        CLEAR, CLOUDS, CLOUDY2, RAIN, SNOW, STORM, UNKNOWN
    }
    private static View view;
    private static String[] windDirrection;

    private static int[] ico = {
            R.mipmap.icons_clear,
            R.mipmap.icons_cloudy,
            R.mipmap.icons_cloudy2,
            R.mipmap.icons_rainy,
            R.mipmap.icons_snowy,
            R.mipmap.icons_stormy,
            R.mipmap.icons_unknown};
    private static int[] img = {
            R.mipmap.weather_clear,
            R.mipmap.weather_cloudy,
            R.mipmap.weather_cloudy,
            R.mipmap.weather_rainy,
            R.mipmap.weather_snowy,
            R.mipmap.weather_stormy,
            R.mipmap.weather_clear
    };

    public static void setView(View view) {
        Weather.view = view;
        windDirrection= view.getResources().getStringArray(R.array.windDirrection);
    }

    public static String getWindDirrection(int ang){
        int id=Math.round(ang/45f);
        return windDirrection[id];
    }

    public static int getIcoFromString(String string) {
        try{
            return ico[WeatherType.valueOf(string).ordinal()];
        }catch (IllegalArgumentException e){
            return ico[6];
        }
    }

    public static int getImgFromString(String weatherType) {
        try{
            return img[WeatherType.valueOf(weatherType).ordinal()];
        }catch (IllegalArgumentException e){
            return img[6];
        }
    }
}
