package com.geekbrains.myweather;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void appSettingsDateTest() {
        long l = AppSettings.get().getToday()*1000;
        //DateTest
        Date curDate = new Date();
        Date checkDate = new Date(l);//!
        DateFormat df = new SimpleDateFormat("yyyy", Locale.US);
        assertEquals(df.format(curDate), df.format(checkDate));
        df = new SimpleDateFormat("MM", Locale.US);
        assertEquals(df.format(curDate), df.format(checkDate));
        df = new SimpleDateFormat("dd", Locale.US);
        assertEquals(df.format(curDate), df.format(checkDate));
        df = new SimpleDateFormat("HH", Locale.US);
        assertEquals("12", df.format(checkDate));
        df = new SimpleDateFormat("mm", Locale.US);
        assertEquals("00", df.format(checkDate));
        //
        boolean expected, actual;
        expected = true;
        AppSettings.get().setInternet(expected);
        actual = AppSettings.get().isInternet();
        assertEquals(actual, expected);

        expected = true;
        AppSettings.get().setSettingHumidity(expected);
        actual = AppSettings.get().isSettingHumidity();
        assertEquals(actual, expected);

        expected = true;
        AppSettings.get().setSettingInFahrenheit(expected);
        actual = AppSettings.get().isSettingInFahrenheit();
        assertEquals(actual, expected);

        expected = true;
        AppSettings.get().setSettingNightMode(expected);
        actual = AppSettings.get().isSettingNightMode();
        assertEquals(actual, expected);

        expected = true;
        AppSettings.get().setSettingPressure(expected);
        actual = AppSettings.get().isSettingPressure();
        assertEquals(actual, expected);

        expected = true;
        AppSettings.get().setSettingWnd(expected);
        actual = AppSettings.get().isSettingWnd();
        assertEquals(actual, expected);

        String expectedText="MyPerfectCity";
        AppSettings.get().setCityName(expectedText);
        String resultText=AppSettings.get().getCityName();
        assertEquals(expectedText,resultText);
    }

    @Test
    public void weatherTest(){
        Weather.getImgUrlFromString("Storm");
        assertEquals(
                Weather.getImgUrlFromString("Storm"),
                "https://images.unsplash.com/photo-1562155618-e1a8bc2eb04f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1080&q=80"
        );
        assertEquals(
                Weather.getIcoFromString("Storm"),
                R.mipmap.icons_stormy
        );
    }

    @Test
    public void converterTest(){
        assertEquals(Converter.convertDateToString(1589792400),"18.05");
        assertEquals(Converter.getHour(1589792400),"12");
    }
}