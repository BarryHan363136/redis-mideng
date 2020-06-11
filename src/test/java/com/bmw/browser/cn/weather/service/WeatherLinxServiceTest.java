package com.bmw.browser.cn.weather.service;

import com.bmw.browser.cn.weather.base.BaseWeatherTest;
import com.bmw.browser.cn.weather.common.utils.CMSApiHelper;
import com.bmw.browser.cn.weather.pojo.navinfoApi.WeatherApiPojo;
import com.bmw.browser.cn.weather.pojo.omc.Geocode;
import com.bmw.browser.cn.weather.pojo.omc.GeocodeList;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class WeatherLinxServiceTest extends BaseWeatherTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestTemplate restTemplate;

    private GeocodeList processingGeocode(String location){
        GeocodeList geocodeList = new GeocodeList();
        Geocode geocode = new Geocode();
        geocode.setId("1");
        geocode.setLocation(location);
        List<Geocode> list = new ArrayList<>();
        list.add(geocode);
        geocodeList.setLocationList(list);
        return geocodeList;
    }

    //@Test
    public void testGetGeocoder(){
        try {
            String location = "上海市";
            GeocodeList geocodeList = processingGeocode(location);

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            requestHeaders.add("ocp-apim-subscription-key", "13AD70DA-923F-4E86-925D-18AF65B0D268");

            HttpEntity<String> requestEntity = new HttpEntity<String>(objectMapper.writeValueAsString(geocodeList), requestHeaders);
            //RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(new URI("https://bmwcommutedev.chinacloudsites.cn/api/GeocodeResponseBean"), requestEntity, String.class);
            if (responseEntity!=null){
                log.info("========>"+responseEntity.getStatusCodeValue()+", "+responseEntity.getBody());
            }
        } catch (Exception e) {
            log.error("testGetGeocoder error {} ", e);
        }
    }

    /**
     * 	/weather/mgu/bmw/infos?lon=114.311582&lat=30.598467&market=cn&language=zh
     * 	https://bonpro.autoai.com/gateway/weather-cms/p1/
     * 	https://bonint.autoai.com/gateway/weather-cms/p1/
     * */
    //@Test
    public void tesGet3rdData() throws IOException {
        String apiUserId = "sh_bmw";
        String apiKey = "523a8fdda97e45e6";

        WeatherApiPojo weatherApiPojo = null;
        String lat = "30.598467";
        String lon = "114.311582";
        String i18n = "zh_CN";
        String queryString = "cata=json&forecasttype=7&lat=" + lat + "&lng=" + lon + "&language=" + i18n + "&userid=" + apiUserId;
        String key = CMSApiHelper.generateHASHKey(queryString, apiKey);
        String requestUrl = "https://bonint.autoai.com/gateway/news-cms/p1/"+"forecastByLocation?" + queryString + "&key=" + key;
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(requestUrl,String.class);
        if (responseEntity!=null && responseEntity.getStatusCodeValue()==200){
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            weatherApiPojo = objectMapper.readValue(responseEntity.getBody(), WeatherApiPojo.class);
            log.info("======================>"+objectMapper.writeValueAsString(weatherApiPojo));
        }
    }


}
