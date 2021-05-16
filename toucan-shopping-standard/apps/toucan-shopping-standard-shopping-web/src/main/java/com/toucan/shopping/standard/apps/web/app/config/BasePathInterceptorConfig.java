package com.toucan.shopping.standard.apps.web.app.config;

import com.toucan.shopping.standard.apps.web.interceptor.BasePathInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class BasePathInterceptorConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BasePathInterceptor basePathInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("初始化BasePath路径拦截器");

        InterceptorRegistration interceptorRegistration = registry.addInterceptor(basePathInterceptor);
        interceptorRegistration.addPathPatterns("/**");
    }


}
