package com.geekbrains.myweather;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CityList {
    private static HashMap<String, CityData> cityList = new HashMap<String, CityData>();

    public static void addCity(CityData city) {
        if(city!=null)cityList.put(city.getCityName(), city);
    }

    public static String[] getSortedCityNames(){
        List<String> sortedList = new ArrayList<>(cityList.keySet());
        Collections.sort(sortedList);
        String[] strings=new String[sortedList.size()];
        sortedList.toArray(strings);
        return strings;
    }

    public static int getLength(){
        return cityList.size();
    }

    public static CityData getCity(String cityName) {
            return cityList.get(cityName);
    }
}
