package com.toucan.shopping.starter.user.sso.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
@ConditionalOnProperty(
        prefix = "toucan.user-auth",
        name = "enabled",
        havingValue = "true"
)
public class UserSSOConfig {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    UserSSOConfig()
    {
        logger.info(" 初始化用户中心单点登录插件..... ");
    }

}
