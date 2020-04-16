package com.geekbrains.myweather;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WeatherInfo {
    private double temperature;
    private int ico;
    private Date date;
    private double pressure;
    private int humidity;
    private int windDirection;
    private double windSpeed;
    private String icoDescription;

    public String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.US);
        return dateFormat.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public String getFormatedTemperature() {
        if(Singleton.getInstance().isSettingInCelsius()){
            return String.format(Locale.getDefault(), "%+.1f" +
                    "\u2103", temperature);
        }else {
            double temperatureFahr=temperature*9/5+32;
            return String.format(Locale.getDefault(), "%+.1f"+
                    "\u2109", temperatureFahr);
        }

    }

    public double getTemperature() {
        return temperature;
    }

    public boolean dateLessThanNow() {
        Date d = new Date();
        return this.date.compareTo(d) > 0;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public int getIco() {
        return ico;
    }

    public String getHour() {
        DateFormat dateFormat = new SimpleDateFormat("HH", Locale.US);
        return dateFormat.format(date);
    }

    public String getIcoDescription() {
        return icoDescription;
    }

    public void setIcoDescription(String icoDescription) {
        this.icoDescription = icoDescription;
        this.ico = Weather.getIcoFromString(icoDescription.toUpperCase());
    }

    public String getPressure() {
        return String.format(Locale.getDefault(), "%+.1f%n", pressure / 1.33333f);
    }

    void setPressure(int pressure) {
        this.pressure = pressure;
    }

    String getHumidity() {
        return humidity + "%";
    }

    void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    String getWindDirection() {
        return Weather.getWindDirrection(windDirection);
    }

    void setWindDirection(int windDirection) {
        this.windDirection = windDirection;
    }

    String getWindSpeed() {
        return String.format(Locale.getDefault(), "%.1f%n", windSpeed);
    }

    void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }
}
