package com.geekbrains.myweather.rest.entities;

import com.google.gson.annotations.SerializedName;

public class WeatherRequestRestModel {
@SerializedName("cod")public int cod;
@SerializedName("message")public int message;
@SerializedName("cnt")public int cnt;
@SerializedName("list")public WeatherListArray[] listArray;
@SerializedName("city")public WeatherCityId cityId;


}
