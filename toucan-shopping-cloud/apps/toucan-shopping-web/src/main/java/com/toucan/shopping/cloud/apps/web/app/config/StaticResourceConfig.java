package com.toucan.shopping.cloud.apps.web.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 静态资源映射
 */
@Component
public class StaticResourceConfig implements WebMvcConfigurer {


    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        logger.info("初始化静态资源.......");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/static/");
    }
}
