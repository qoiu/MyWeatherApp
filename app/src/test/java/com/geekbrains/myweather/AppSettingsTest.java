package com.geekbrains.myweather;

import com.geekbrains.myweather.model.AppSettings;
import com.geekbrains.myweather.model.ConverterDate;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AppSettingsTest {

private Date curDate,checkDate;

    private boolean expected, actual;
    private DateFormat df;
    private long l = AppSettings.get().getToday()*1000;

    @Before
    public void prepare() {
       /* curDate = new Date();
        checkDate = new Date();//!*/
        l = AppSettings.get().getToday()*1000;
        curDate = new Date();
        checkDate = new Date(l);//!
        df = new SimpleDateFormat("yyyy.MM.dd-HH:mm", Locale.US);
        System.out.println(df.format(curDate));
        System.out.println(df.format(checkDate));

    }

    @Test
    public void get() {
        assertNotEquals(null,AppSettings.get());
    }

    @Test
    public void setInternet() {
        boolean expected, actual;
        expected = true;
        AppSettings.get().setInternet(expected);
        actual = AppSettings.get().isInternet();
        assertEquals(actual, expected);
    }

    @Test
    public void setSettingNightMode() {
        expected = true;
        AppSettings.get().setSettingNightMode(expected);
        actual = AppSettings.get().isSettingNightMode();
        assertEquals(actual, expected);
    }

    @Test
    public void setSettingWnd() {
        expected = true;
        AppSettings.get().setSettingWnd(expected);
        actual = AppSettings.get().isSettingWnd();
        assertEquals(actual, expected);
    }

    @Test
    public void setSettingPressure() {
        expected = true;
        AppSettings.get().setSettingPressure(expected);
        actual = AppSettings.get().isSettingPressure();
        assertEquals(actual, expected);
    }

    @Test
    public void setSettingHumidity() {
        expected = true;
        AppSettings.get().setSettingHumidity(expected);
        actual = AppSettings.get().isSettingHumidity();
        assertEquals(actual, expected);

        expected = false;
        AppSettings.get().setSettingInFahrenheit(expected);
        actual = AppSettings.get().isSettingInFahrenheit();
        assertEquals(actual, expected);

    }

    @Test
    public void setSettingInFahrenheit() {
        expected = true;
        AppSettings.get().setSettingInFahrenheit(expected);
        actual = AppSettings.get().isSettingInFahrenheit();
        assertEquals(actual, expected);

        expected = false;
        AppSettings.get().setSettingInFahrenheit(expected);
        actual = AppSettings.get().isSettingInFahrenheit();
        assertEquals(actual, expected);

    }

    @Test
    public void setCityName() {
        String expectedText="MyPerfectCity";
        AppSettings.get().setCityName(expectedText);
        String resultText=AppSettings.get().getCityName();
        assertEquals(expectedText,resultText);
    }

    @Test
    public void getLocation() {
    }

    @Test
    public void setLocation() {
    }

    @Test
    public void setLocalization() {
        String expected="en";
        AppSettings.get().setLocalization(expected);
        assertEquals(expected,AppSettings.get().getLocalization());
    }

    @Test
    public void getTodayCheckYear() {
        df = new SimpleDateFormat("yyyy", Locale.US);
        assertEquals(df.format(curDate), df.format(checkDate));
    }

    @Test
    public void getTodayCheckMonth() {
        df = new SimpleDateFormat("MM", Locale.US);
        assertEquals(df.format(curDate), df.format(checkDate));
    }

    @Test
    public void getTodayCheckDays() {
        df = new SimpleDateFormat("dd", Locale.US);
        assertEquals(df.format(curDate), df.format(checkDate));
    }

    @Test
    public void getTodayCheckHours() {
        df = new SimpleDateFormat("HH", Locale.US);
        assertEquals("12", df.format(checkDate));
    }

    @Test
    public void getTodayCheckMinutes() {
        df = new SimpleDateFormat("mm", Locale.US);
        assertEquals("00", df.format(checkDate));
    }

    @Test
    public void setToday() {
        AppSettings.get().setToday(curDate.getTime());
        System.out.println(df.format(AppSettings.get().getToday()));
        assertEquals(AppSettings.get().getToday(), curDate.getTime());
        AppSettings.get().setToday( ConverterDate.getDefineHour(new Date().getTime()/1000,12));
    }
}