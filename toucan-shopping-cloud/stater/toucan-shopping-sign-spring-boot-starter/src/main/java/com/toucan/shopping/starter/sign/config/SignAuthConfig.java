package com.toucan.shopping.starter.sign.config;

import com.toucan.shopping.starter.sign.filter.RequestWrapperFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@ConditionalOnProperty(
        prefix = "toucan.sign",
        name = "enabled",
        havingValue = "true"
)
@EnableDiscoveryClient
public class SignAuthConfig {

    /**
     * 注册替换request对象过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(signStarterRequestWrapperFilter());
        registration.addUrlPatterns("/*");
        registration.setName("signStarterRequestWrapperFilterRegistrationBean");
        return registration;
    }

    @Bean(name = "signStarterRequestWrapperFilter")
    public Filter signStarterRequestWrapperFilter() {
        return new RequestWrapperFilter();
    }

}
