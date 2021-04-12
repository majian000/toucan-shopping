package com.toucan.shopping.cloud.user.web.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan("com.toucan.shopping")
@EnableFeignClients(basePackages = "com.toucan.shopping")
@EnableHystrix
public class UserCenterWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterWebApplication.class, args);
    }
}
