package com.toucan.shopping.cloud.apps.user.web.app.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * feign http请求开启打印日志
 */
@Configuration
public class OpenFeignLogConfig {

    @Bean
    Logger.Level feignLoggerLeave(){
        return Logger.Level.FULL;
    }

}
