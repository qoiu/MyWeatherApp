package com.geekbrains.myweather;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public static boolean isNoon(long date){
        int hour=Integer.parseInt(getHour(date));
        return (hour>=12 && hour<15);
    }

    public static String geFullDate(long date){
        DateFormat dateFormat = new SimpleDateFormat("HH", Locale.US);
        Date d=new Date(date*1000);
        return d.toString();
    }

    public static long getNoon(long lDate){
        Date d=new Date(lDate*1000);
        DateFormat df=new SimpleDateFormat("yyyy.MM.dd 12:00:00", Locale.US);
        String s=df.format(d);
        df=new SimpleDateFormat("yyyy.MM.dd HH:mm:SS", Locale.US);
        Date date;
        try {
            date=df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            return d.getTime();
        }
        return date.getTime()/1000;
    }

}
