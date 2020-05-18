package com.geekbrains.myweather;

public class MainInformationData {
    private String cityNameValue;
    private String imgUrl;
    private String temperature;
    private float temperatureValue;

    public String getCityNameValue() {
        return cityNameValue;
    }

    public void setCityNameValue(String cityNameValue) {
        this.cityNameValue = cityNameValue;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public float getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(float temperatureValue) {
        this.temperatureValue = temperatureValue;
    }
}
