package com.bmw.browser.cn.weather.controller;

import com.bmw.browser.cn.common.enums.MetaStatusEnum;
import com.bmw.browser.cn.common.utils.ConstantsUtils;
import com.bmw.browser.cn.filter.filter.ResponsePojoEVOFilter;
import com.bmw.browser.cn.filter.filter.ResponsePojoMGUFilter;
import com.bmw.browser.cn.weather.base.BaseWeatherTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class WeatherControllerTest extends BaseWeatherTest {
    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    private HttpHeaders headers;
    @Before
    public void setup() {
        headers = new HttpHeaders();
        headers.add("bmw-vin","B2V2000");
        headers.add("bmw-b2v_url","https://oap-int.bmw.com.cn");
        headers.add("user-agent","Mozilla/5.0 (NBTEvo_ASN;07-15;Main;1440x540;gps nav;;zh_CN;tts;zh_CN;p-sim;;;;;;;;;) AppleWebKit/537.6 (KHTML, like Gecko)");
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new ResponsePojoMGUFilter(), "/weather/mgu/*")
                .addFilter(new ResponsePojoEVOFilter(), "/weather/evo/*").build();
    }

    /**
     * 1、mockMvc.perform执行一个请求。
     * 2、MockMvcRequestBuilders.get("XXX")构造一个请求。
     * 3、ResultActions.param添加请求传值
     * 4、ResultActions.accept(MediaType.TEXT_HTML_VALUE))设置返回类型
     * 5、ResultActions.andExpect添加执行完成后的断言。
     * 6、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情
     *   比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
     * 5、ResultActions.andReturn表示执行完成后返回相应的结果。
     */

    /**
     * 1
     * 经纬度小数点后超3位，中文测试
     * 提供车机MGU天气信息,此接口是现阶段使用的接口服务
     * @throws Exception
     */
    @Test
    public void getWeatherInfosForMGUBMW() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/mgu/bmw/infos")
                .param("lon","114.3115822323").param("lat","30.5984623237")
                .param("market","cn").param("language",ConstantsUtils.LANGUAGE_ZH_L)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..language").value(ConstantsUtils.LANGUAGE_ZH_L))
                .andExpect(MockMvcResultMatchers.jsonPath("$..status").value(MetaStatusEnum.OK.getMetaStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$..weatherList").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$..weatherList.length()").value(5))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 2
     * 经纬度小数点后少于3位，英文测试
     * 提供车机MGU天气信息,此接口是现阶段使用的接口服务
     * @throws Exception
     */
    @Test
    public void getWeatherInfosForMGUBMW2() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/mgu/bmw/infos")
                .param("lon","114.3").param("lat","30.59")
                .param("market","cn").param("language",ConstantsUtils.LANGUAGE_EN_L)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..language").value(ConstantsUtils.LANGUAGE_EN_L))
                .andExpect(MockMvcResultMatchers.jsonPath("$..status").value(MetaStatusEnum.OK.getMetaStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$..weatherList.length()").value(5))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 3
     * 无效经纬度，language为非法值
     * 提供车机MGU天气信息,此接口是现阶段使用的接口服务
     * @throws Exception
     */
    @Test
    public void getWeatherInfosForMGUBMW3() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/mgu/bmw/infos")
                .param("lon","139.69").param("lat","35.68")
                .param("market","cn").param("language","no")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..language").value(ConstantsUtils.LANGUAGE_ZH_L))
                .andExpect(MockMvcResultMatchers.jsonPath("$..status").value(MetaStatusEnum.INTERNAL_ERROR.getMetaStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(0))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 4
     * 提供车机MGU天气信息,此接口是根据经纬度获取城市名
     * @throws Exception
     */
    @Test
    public void reverseGeocoderForMGUBMW() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/mgu/bmw/reverseGeocoder")
                .param("lon","114.31").param("lat","30.59")
                .param("market","cn").param("language",ConstantsUtils.LANGUAGE_EN_L)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..language").value(ConstantsUtils.LANGUAGE_EN_L))
                .andExpect(MockMvcResultMatchers.jsonPath("$..city").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$..district").isNotEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    /**
     * 5
     * 无效经纬度
     * 提供车机MGU天气信息,此接口是根据经纬度获取城市名
     * @throws Exception
     */
    @Test
    public void reverseGeocoderForMGUBMW2() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/mgu/bmw/reverseGeocoder")
                .param("lon","139.69").param("lat","35.68")
                .param("market","cn").param("language",ConstantsUtils.LANGUAGE_EN_L)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data").isEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 6
     * 提供车机MGU天气信息,此接口是根据城市名获取经纬度
     * @throws Exception
     */
    @Test
    public void getGeocoderForMGUBMW() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/mgu/bmw/geocoder")
                .param("location","上海市")
                .param("market","cn").param("language",ConstantsUtils.LANGUAGE_EN_L)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..language").value(ConstantsUtils.LANGUAGE_EN_L))
                .andExpect(MockMvcResultMatchers.jsonPath("$..lat").isNotEmpty())
                .andExpect(MockMvcResultMatchers.jsonPath("$..lon").isNotEmpty())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 7 无效地址
     * 提供车机MGU天气信息,此接口是根据城市名获取经纬度
     * @throws Exception
     */
    @Test
    public void getGeocoderForMGUBMW2() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/mgu/bmw/geocoder")
                .param("location","shanghaishi")
                .param("market","cn").param("language",ConstantsUtils.LANGUAGE_EN_L)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..language").value(ConstantsUtils.LANGUAGE_EN_L))
                .andExpect(MockMvcResultMatchers.jsonPath("$..lat").value("0.0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$..lon").value("0.0"))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    /**
     * 8
     * 提供车机ID5,ID6天气信息 and MINI
     * @throws Exception
     */
    @Test
    public void getWeatherInfosForEVO() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/evo/bmw/infos")
                .param("lon","114.31").param("lat","30.5")
                .param("market","cn").param("language",ConstantsUtils.LANGUAGE_EN_L)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..session.language").value(ConstantsUtils.LANGUAGE_EN_L))
                .andExpect(MockMvcResultMatchers.jsonPath("$..status").value(MetaStatusEnum.OK.getMetaStatus()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.length()").value(5))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
    /**
     * 9
     * 无效经纬度
     * 提供车机ID5,ID6天气信息 and MINI
     * @throws Exception
     */
    @Test
    public void getWeatherInfosForEVO2() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/weather/evo/bmw/infos")
                .param("lon","139.69").param("lat","35.68")
                .param("market","cn").param("language",ConstantsUtils.LANGUAGE_EN_L)
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$..status").value(MetaStatusEnum.INVALID_PARAMETER.getMetaStatus()))
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}
