package com.toucan.shopping.cloud.apps.scheduler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("com.toucan.shopping")
public class CloudSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSchedulerApplication.class, args);
    }



}
