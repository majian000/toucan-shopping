package com.toucan.shopping.cloud.product.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@MapperScan({"com.toucan.shopping.modules.product.mapper","com.toucan.shopping.modules.common.persistence.mapper"})
@ComponentScan("com.toucan.shopping")
public class CloudProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudProductServiceApplication.class, args);
    }

}
