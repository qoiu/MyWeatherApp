package com.geekbrains.myweather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class LocationModule {
    @SuppressLint("StaticFieldLeak")
    private static LocationModule instance;
    private LocationManager locManager;
    private Geocoder geo;
    private Location loc;

    private LocationModule() {
    }

    public static LocationModule getInstance(){
        if(instance==null)instance = new LocationModule();
        return instance;
    }

    public void setLocManager(LocationManager locManager){
        this.locManager=locManager;
    }
    public void setFromActivity(Activity activity){
        geo=new Geocoder(activity);
    }

    private LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            loc=location;
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };

    @SuppressLint("MissingPermission")
    public Location getLocation() {
        if (locManager != null) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,2800, 1, locationListener);
            try {

                loc = Objects.requireNonNull(locManager)
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } catch (Exception e) {
                Log.e("LocationData", e.toString());
            }
            loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            loc = locManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            return loc;
        }
        return null;
    }


    public String getCityByLoc(Location loc) {
        if(loc==null)return "";
        List<Address> list = null;
        try {
            list = geo.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
        } catch (IOException e) {
            Log.e("LocationData", e.toString());
            return "";
        }
        if (list == null || list.isEmpty() || list.get(0).getLocality() == null) return "MSG_NO_DATA";
        return list.get(0).getLocality();
    }


}
