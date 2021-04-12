package com.toucan.shopping.cloud.product.app.config;

import com.toucan.shopping.cloud.product.interceptor.SignInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SignInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private SignInterceptor signInterceptor;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("初始化应用签名拦截器...............");

        InterceptorRegistration interceptorRegistration = registry.addInterceptor(signInterceptor);
        interceptorRegistration.addPathPatterns("/**");
    }


}
