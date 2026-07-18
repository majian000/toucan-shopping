package com.toucan.shopping.starter.apiMonitor.config;

import com.toucan.shopping.starter.apiMonitor.interceptor.ApiMonitorInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 接口监控 Web 配置（单独类，避免循环依赖）
 */
@AutoConfiguration
@ConditionalOnBean(ApiMonitorInterceptor.class)
public class ApiMonitorWebConfig implements WebMvcConfigurer {

    @Autowired
    private ApiMonitorInterceptor apiMonitorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiMonitorInterceptor).order(0);
    }
}
