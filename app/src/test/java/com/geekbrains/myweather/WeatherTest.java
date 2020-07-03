package com.geekbrains.myweather;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(Resources.class)
public class WeatherTest {

    @Before
    public void before(){
        String[] myNewArray = {"С","С-В","В"};
        Weather.setWindDirection(myNewArray);
    }
    @Test
    public void weatherGetImgUrlTestCorrect() {
        Weather.getImgUrlFromString("Storm");
        assertEquals(
                Weather.getImgUrlFromString("Storm"),
                "https://images.unsplash.com/photo-1562155618-e1a8bc2eb04f?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1080&q=80"
        );
    }

    @Test
    public void weatherGetImgUrlTestIncorrect() {
        assertEquals(
                Weather.getImgUrlFromString("Storming"),
                "https://images.unsplash.com/photo-1494548162494-384bba4ab999?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=crop&w=1080&q=80"
        );
    }

    @Test
    public void weatherGetIcoTestCorrect() {
        assertEquals(
                Weather.getIcoFromString("Storm"),
                R.mipmap.icons_stormy
        );
    }

    @Test
    public void weatherGetIcoTestIncorrect() {
        assertEquals(
                Weather.getIcoFromString("Storming"),
                R.mipmap.icons_unknown
        );
    }

    @Test
    public void getWindDirection() {
        String result=Weather.getWindDirection(40);
        assertEquals("С-В",result);
    }
}
