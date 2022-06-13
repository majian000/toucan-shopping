package com.toucan.shopping.starter.admin.auth.config;

import com.toucan.shopping.starter.admin.auth.filter.RequestWrapperFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@ConditionalOnProperty(
        prefix = "toucan.admin-auth",
        name = "enabled",
        havingValue = "true"
)
@EnableEurekaClient
public class AdminAuthConfig {

    /**
     * 注册替换request对象过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(adminAuthStarterRequestWrapperFilter());
        registration.addUrlPatterns("/*");
        registration.setName("adminAuthStarterRequestWrapperFilterRegistrationBean");
        return registration;
    }

    @Bean(name = "adminAuthStarterRequestWrapperFilter")
    public Filter adminAuthStarterRequestWrapperFilter() {
        return new RequestWrapperFilter();
    }

}
