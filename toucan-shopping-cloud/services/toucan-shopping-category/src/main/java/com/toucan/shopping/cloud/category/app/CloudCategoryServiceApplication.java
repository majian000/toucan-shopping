package com.toucan.shopping.cloud.category.app;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.toucan.shopping")
@MapperScan({"com.toucan.shopping.modules.category.mapper",
        "com.toucan.shopping.modules.common.persistence.mapper"})
@ComponentScan("com.toucan.shopping")
public class CloudCategoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudCategoryServiceApplication.class, args);
    }

}
