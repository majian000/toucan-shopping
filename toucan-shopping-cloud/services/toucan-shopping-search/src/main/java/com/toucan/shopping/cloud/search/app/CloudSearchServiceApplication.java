package com.toucan.shopping.cloud.search.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.toucan.shopping")
@ComponentScan("com.toucan.shopping")
public class CloudSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSearchServiceApplication.class, args);
    }

}
