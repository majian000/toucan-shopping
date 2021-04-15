package com.toucan.shopping.cloud.admin.auth.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan({"com.toucan.shopping.modules.admin.auth.mapper",
        "com.toucan.shopping.modules.common.persistence.mapper"})
@ComponentScan("com.toucan.shopping")
public class CloudAdminAuthServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudAdminAuthServiceApplication.class, args);
    }
}
