package com.toucan.shopping.starter.user.auth.config;

import com.toucan.shopping.starter.user.auth.filter.RequestWrapperFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@ConditionalOnProperty(
        prefix = "toucan.user-auth",
        name = "enabled",
        havingValue = "true"
)
@EnableDiscoveryClient
public class UserAuthConfig {

    /**
     * 注册替换request对象过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(userCenterStarterRequestWrapperFilter());
        registration.addUrlPatterns("/*");
        registration.setName("userAuthStarterRequestWrapperFilterRegistrationBean");
        return registration;
    }

    @Bean(name = "userAuthStarterRequestWrapperFilter")
    public Filter userCenterStarterRequestWrapperFilter() {
        return new RequestWrapperFilter();
    }

}
