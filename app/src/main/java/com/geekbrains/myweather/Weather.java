package com.geekbrains.myweather;

public class Weather {

    private static int screenWidth=1080;

    enum WeatherType {
        CLEAR, CLOUDS, CLOUDY2, RAIN, SNOW, STORM, UNKNOWN
    }

    private static String[] windDirection;

    private static int[] ico = {
            R.mipmap.icons_clear,
            R.mipmap.icons_cloudy,
            R.mipmap.icons_cloudy2,
            R.mipmap.icons_rainy,
            R.mipmap.icons_snowy,
            R.mipmap.icons_stormy,
            R.mipmap.icons_unknown};

    private static String[] url={
            "https://images.unsplash.com/photo-1494548162494-384bba4ab999?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w="+screenWidth+"&q=80",
            "https://images.unsplash.com/photo-1561634666-669fe33c3d0a?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w="+screenWidth+"&q=80",
            "https://images.unsplash.com/photo-1558999266-d2f6674216fa?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w="+screenWidth+"&q=80",
            "https://images.unsplash.com/photo-1534274988757-a28bf1a57c17?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w="+screenWidth+"&q=80",
            "https://images.unsplash.com/photo-1576357852201-6f5791fe1619?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w="+screenWidth+"&q=80",
            "https://images.unsplash.com/photo-1562155618-e1a8bc2eb04f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w="+screenWidth+"&q=80"
    };

    public static void setWindDirection(String[] windDirection) {
        Weather.windDirection = windDirection;
    }

    public static String getWindDirection(int ang){
        int id=Math.round(ang/45f);
        return windDirection[id];
    }

    public static int getIcoFromString(String string) {
        try{
            return ico[WeatherType.valueOf(string.toUpperCase()).ordinal()];
        }catch (IllegalArgumentException e){
            return ico[6];
        }
    }

    public static String getImgUrlFromString(String weatherType){
        try{
            return url[WeatherType.valueOf(weatherType.toUpperCase()).ordinal()];
        }catch (IllegalArgumentException e){
            return url[0];
        }
    }
}
