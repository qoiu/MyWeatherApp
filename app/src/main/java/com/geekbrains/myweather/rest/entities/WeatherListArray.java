package com.geekbrains.myweather.rest.entities;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class WeatherListArray {
    @SerializedName("dt")public long dt;
    @SerializedName("main")public WeatherMainData weatherMainData;
    @SerializedName("weather")public WeatherExtraData[] weatherExtraData;
    @SerializedName("clouds")public CloudsRestModel cloudsRestModel;
    @SerializedName("wind")public WindRestModel windRestModel;
    @SerializedName("sys")public SysRestModel sysRestModel;
    @SerializedName("dt_txt")public String dtText;


}
