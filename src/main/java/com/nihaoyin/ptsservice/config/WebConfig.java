package com.nihaoyin.ptsservice.config;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

// 全局配置跨域请求
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD")
                .allowedOrigins("http://localhost:8080", "null")
                .allowCredentials(true) // 允许携带token之类的东西
                .maxAge(7200);
    }
}
