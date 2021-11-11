package com.toucan.shopping.cloud.apps.seller.web.app.config;

import com.toucan.shopping.cloud.apps.seller.web.interceptor.ShopAuthInterceptor;
import com.toucan.shopping.starter.user.auth.interceptor.AuthInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ShopAuthInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private ShopAuthInterceptor shopAuthInterceptor;


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("初始化店铺权限拦截器 拦截路径 /**");
        InterceptorRegistration interceptorRegistration = registry.addInterceptor(shopAuthInterceptor);
        interceptorRegistration.addPathPatterns("/**");
    }


}
