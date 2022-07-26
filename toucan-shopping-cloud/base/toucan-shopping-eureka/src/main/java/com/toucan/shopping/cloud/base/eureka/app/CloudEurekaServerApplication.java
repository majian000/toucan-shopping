package com.toucan.shopping.cloud.base.eureka.app;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@Slf4j
@EnableEurekaServer
@SpringBootApplication
public class CloudEurekaServerApplication {
    public static void main(String[] args) {
      log.info("服务注册中心已经替换成nacos");
//        SpringApplication.run(CloudEurekaServerApplication.class, args);
    }
}
