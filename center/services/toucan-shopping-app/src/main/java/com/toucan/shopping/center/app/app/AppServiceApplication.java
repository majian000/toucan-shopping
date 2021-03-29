package com.toucan.shopping.center.app.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan({"com.toucan.shopping.center.app.app.mapper",
        "com.toucan.shopping.common.persistence.mapper"})
@ComponentScan("com.toucan.shopping")
public class AppServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AppServiceApplication.class, args);
    }
}
