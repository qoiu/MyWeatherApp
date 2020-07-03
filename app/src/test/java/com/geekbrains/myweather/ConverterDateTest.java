package com.geekbrains.myweather;

import com.geekbrains.myweather.model.ConverterDate;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ConverterDateTest {


    @Test
    public void convertDateToString() {
        assertEquals(ConverterDate.extract(1589792400,"dd.MM"),"18.05");
    }

    @Test
    public void getHour() {
        assertEquals(ConverterDate.extract(1589792400,"HH"),"12");
    }


    @Test
    public void isNoon() {
        assertFalse(ConverterDate.isNoon(1587612000));
        assertTrue(ConverterDate.isNoon(1587632400));
    }
/*
    @Test
    public void geFullDate() {

    }
*/
    @Test
    public void getNoon() {
        assertEquals(ConverterDate.getDefineHour(1587612000,12),1587632400);
    }
}