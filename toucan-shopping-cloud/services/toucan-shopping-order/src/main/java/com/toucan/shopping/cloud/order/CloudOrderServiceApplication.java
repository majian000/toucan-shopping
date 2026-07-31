package com.toucan.shopping.cloud.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@MapperScan({"com.toucan.shopping.modules.order.mapper","com.toucan.shopping.modules.common.persistence.mapper","com.toucan.shopping.modules.common.persistence.event.mapper"})
@EnableFeignClients(basePackages = "com.toucan.shopping")
@ComponentScan("com.toucan.shopping")
public class CloudOrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudOrderServiceApplication.class, args);
    }
}
