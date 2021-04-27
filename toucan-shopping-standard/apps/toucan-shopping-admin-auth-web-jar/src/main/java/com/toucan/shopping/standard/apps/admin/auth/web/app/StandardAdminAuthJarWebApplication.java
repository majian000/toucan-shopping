package com.toucan.shopping.standard.apps.admin.auth.web.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@ComponentScan("com.toucan.shopping")
public class StandardAdminAuthJarWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(StandardAdminAuthJarWebApplication.class, args);
    }

}
