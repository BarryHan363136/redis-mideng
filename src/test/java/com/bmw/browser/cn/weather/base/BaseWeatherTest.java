package com.bmw.browser.cn.weather.base;


import com.bmw.browser.cn.weather.WeatherServiceStarter;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

//@RunWith(SpringJUnit4ClassRunner.class)
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WeatherServiceStarter.class)
@WebAppConfiguration
public class BaseWeatherTest {


    @Ignore
    @Test
    public void baseTest(){
    }

}