package com.toucan.shopping.cloud.seller.app;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
@MapperScan({"com.toucan.shopping.modules.seller.mapper",
        "com.toucan.shopping.modules.common.persistence.mapper"})
@ComponentScans({@ComponentScan("com.toucan.shopping")})
public class CloudSellerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSellerServiceApplication.class, args);
    }
}
