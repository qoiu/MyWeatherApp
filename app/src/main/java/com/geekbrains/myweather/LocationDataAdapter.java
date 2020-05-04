package com.geekbrains.myweather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;

import java.io.IOException;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class LocationDataAdapter {
    private static LocationManager locManager;
    private static Geocoder geo;

    LocationDataAdapter(Context context) {
        geo= new Geocoder(context);
        locManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
    public static Location getLocation() {
        Location loc;
        if (locManager != null) {
            loc = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            return loc;
        }
        return null;
    }

    public static String getCityByLoc(Location loc) {
        List<Address> list;
        try {
            list = geo.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        if (list.isEmpty()) return "MSG_NO_DATA";
        return list.get(0).getLocality();
    }
}
