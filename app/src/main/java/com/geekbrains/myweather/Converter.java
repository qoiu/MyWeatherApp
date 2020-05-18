package com.geekbrains.myweather;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Converter {

    public static Location latLngToLocation(LatLng latLng){
        Location location = new Location("convert");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }

    public static String convertDateToString(long date) {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM", Locale.US);
        return dateFormat.format(date*1000);
    }

    public static String getHour(long date){
        DateFormat dateFormat = new SimpleDateFormat("HH", Locale.US);
        return dateFormat.format(date*1000);
    }

}
