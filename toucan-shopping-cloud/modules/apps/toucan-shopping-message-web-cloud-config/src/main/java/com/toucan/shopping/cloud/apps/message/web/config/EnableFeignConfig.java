package com.toucan.shopping.cloud.apps.message.web.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.toucan.shopping")
@Configuration
public class EnableFeignConfig {


}
