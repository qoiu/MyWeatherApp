package com.geekbrains.myweather.rest.entities;

import com.google.gson.annotations.SerializedName;

public class WeatherCityId {
    @SerializedName("id")public int id;
    @SerializedName("name")public String name;
    @SerializedName("country")public String country;
    @SerializedName("cord")public CordRestModel cordRestModel;
    @SerializedName("population")public long population;
    @SerializedName("sunrise")public long sunrise;
    @SerializedName("sunset")public long sunset;
    @SerializedName("timezone")public int timezone;

}
