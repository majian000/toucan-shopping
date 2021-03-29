package com.toucan.shopping.starter.center.user.config;

import com.toucan.shopping.starter.center.user.filter.RequestWrapperFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@ConditionalOnProperty(
        prefix = "toucan.user-center",
        name = "enabled",
        havingValue = "true"
)
@EnableEurekaClient
public class UserCenterConfig {

    /**
     * 注册替换request对象过滤器
     * @return
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(bbsUserCenterStarterRequestWrapperFilter());
        registration.addUrlPatterns("/*");
        registration.setName("bbsUserCenterStarterRequestWrapperFilterRegistrationBean");
        return registration;
    }

    @Bean(name = "bbsUserCenterStarterRequestWrapperFilter")
    public Filter bbsUserCenterStarterRequestWrapperFilter() {
        return new RequestWrapperFilter();
    }

}
