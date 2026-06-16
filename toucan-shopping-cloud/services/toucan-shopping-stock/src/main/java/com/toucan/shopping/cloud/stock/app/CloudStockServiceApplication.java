package com.toucan.shopping.cloud.stock.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan({"com.toucan.shopping.modules.stock.mapper","com.toucan.shopping.modules.common.persistence.mapper","com.toucan.shopping.modules.common.persistence.event.mapper"})
@ComponentScan("com.toucan.shopping")
public class CloudStockServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudStockServiceApplication.class, args);
    }

}
