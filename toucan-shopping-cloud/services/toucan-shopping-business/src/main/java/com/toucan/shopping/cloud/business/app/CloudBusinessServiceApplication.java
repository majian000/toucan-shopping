package com.toucan.shopping.cloud.business.app;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
@MapperScan({"com.toucan.shopping.modules.business.mapper",
        "com.toucan.shopping.modules.common.persistence.mapper"})
@ComponentScans({@ComponentScan("com.toucan.shopping")})
public class CloudBusinessServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudBusinessServiceApplication.class, args);
    }
}
