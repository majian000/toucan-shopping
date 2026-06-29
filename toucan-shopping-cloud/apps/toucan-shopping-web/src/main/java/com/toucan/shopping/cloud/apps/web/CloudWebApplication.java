package com.toucan.shopping.cloud.apps.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("com.toucan.shopping")
public class CloudWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudWebApplication.class, args);
    }



}
