package com.geekbrains.myweather.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public class ConverterLocation {

    public static Location latLngToLocation(LatLng latLng){
        Location location = new Location("convert");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);
        return location;
    }
}
