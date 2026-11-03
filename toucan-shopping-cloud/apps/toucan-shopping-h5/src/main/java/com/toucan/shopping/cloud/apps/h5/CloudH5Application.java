package com.toucan.shopping.cloud.apps.h5;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan("com.toucan.shopping")
public class CloudH5Application {

    public static void main(String[] args) {
        SpringApplication.run(CloudH5Application.class, args);
    }

}
