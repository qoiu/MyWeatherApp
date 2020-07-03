package com.geekbrains.myweather;


import com.geekbrains.myweather.model.AppSettings;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

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
    public void ThreeTest(){
        Assert.assertEquals(3,calc(5));
        System.out.println(new Date(AppSettings.get().getToday()*1000).toString());
    }

    private int calc(int a){
        return (int) (Math.floor(a/3)*3);
    }

}