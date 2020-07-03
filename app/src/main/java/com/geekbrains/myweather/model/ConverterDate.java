package com.geekbrains.myweather.model;

import com.geekbrains.myweather.rest.model.WeatherInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ConverterDate {

    @Provides
    @Singleton
    public static String extract(long date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        return dateFormat.format(date*1000);
    }

    @Provides
    @Singleton
    public static boolean isNoon(long date){
        return extract(date,"HH").equals("12");
    }

    @Provides
    @Singleton
    public static long getDefineHour(long lDate,int hour){
        Date d=new Date(lDate*1000);
        String sHour;
        if(hour>0 && hour<10){
            sHour="0"+hour;
        }else if(hour<24){
            sHour=String.valueOf(hour);
        }else{sHour="00";}
        DateFormat df=new SimpleDateFormat("yyyy.MM.dd "+sHour+":00:00", Locale.US);
        String s=df.format(d);
        df=new SimpleDateFormat("yyyy.MM.dd HH:mm:SS", Locale.US);
        Date date;
        try{
            date=df.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
            return d.getTime();
        }
        return date.getTime()/1000;
    }

    @Provides
    @Singleton
    public static WeatherInfo formatTitle(WeatherInfo weatherInfo, boolean withDate) {
        if(ConverterDate.extract(weatherInfo.date,"HH").equals("00")||withDate){
            weatherInfo.setTitle(ConverterDate.extract(weatherInfo.date,"dd.MM")+"\n");
        }
        weatherInfo.setTitle(weatherInfo.getTitle()+ ConverterDate.extract(weatherInfo.date,"HH")+":00");
        return weatherInfo;
    }
}
