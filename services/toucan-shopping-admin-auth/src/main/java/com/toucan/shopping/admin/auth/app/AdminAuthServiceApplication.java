package com.toucan.shopping.admin.auth.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan({"com.toucan.shopping.admin.auth.mapper",
        "com.toucan.shopping.common.persistence.mapper"})
@ComponentScan("com.toucan.shopping")
public class AdminAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminAuthServiceApplication.class, args);
    }
}
