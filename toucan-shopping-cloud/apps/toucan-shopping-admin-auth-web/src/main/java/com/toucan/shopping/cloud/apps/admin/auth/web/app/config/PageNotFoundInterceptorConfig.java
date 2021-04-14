package com.toucan.shopping.cloud.apps.admin.auth.web.app.config;

import com.toucan.shopping.cloud.apps.admin.auth.web.interceptor.PageNotFoundInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PageNotFoundInterceptorConfig implements WebMvcConfigurer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PageNotFoundInterceptor pageNotFoundInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        logger.info("初始化404拦截器");

        InterceptorRegistration interceptorRegistration = registry.addInterceptor(pageNotFoundInterceptor);
        interceptorRegistration.addPathPatterns("/**");
    }


}
