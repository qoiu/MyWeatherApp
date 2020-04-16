package com.geekbrains.myweather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CityList {
    private static HashMap<String, CityData> cityList = new HashMap<String, CityData>();
    public static final CityData CITY_NOT_FOUND=new CityData("NotFound");

    static void addCity(CityData city) {
        if(city!=null){
            cityList.put(city.getCityName().toLowerCase(), city);
        }
    }

    static void addCity(String cityName){
        if(cityList.get(cityName)==null){
            cityList.put(cityName.toLowerCase(),CITY_NOT_FOUND);
        }
    }

    static String[] getSortedCityNames(){
        List<String> sortedList = new ArrayList<>();
        for (HashMap.Entry l:cityList.entrySet()){
            if(l.getValue()!=CITY_NOT_FOUND)sortedList.add(((CityData)l.getValue()).getCityName());
        }
        Collections.sort(sortedList);
        String[] strings=new String[sortedList.size()];
        sortedList.toArray(strings);
        return strings;
    }

    public static int getLength(){
        return cityList.size();
    }

    public static CityData getCity(String cityName) {
            return cityList.get(cityName.toLowerCase());
    }
}
