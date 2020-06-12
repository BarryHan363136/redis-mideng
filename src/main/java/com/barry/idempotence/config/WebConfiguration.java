package com.barry.idempotence.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

@Configuration
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Resource
    private AutoIdempotentInterceptor autoIdempotentInterceptor;

    /**
     * 跨域
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    /**
     * 添加拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(autoIdempotentInterceptor);
        super.addInterceptors(registry);
    }

    /**
     @Override
     public void addInterceptors(InterceptorRegistry registry) {
     // 接口幂等性拦截器
     registry.addInterceptor(apiIdempotentInterceptor());
     super.addInterceptors(registry);
     }

     @Bean
     public ApiIdempotentInterceptor apiIdempotentInterceptor() {
     return new ApiIdempotentInterceptor();
     }
     * */

}