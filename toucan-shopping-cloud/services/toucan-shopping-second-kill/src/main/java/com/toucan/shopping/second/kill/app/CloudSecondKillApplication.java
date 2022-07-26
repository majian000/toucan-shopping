package com.toucan.shopping.second.kill.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan({"com.toucan.shopping.modules.common.persistence.mapper"})
@EnableFeignClients(basePackages = "com.toucan.shopping")
@ComponentScan("com.toucan.shopping")
public class CloudSecondKillApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudSecondKillApplication.class, args);
    }


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return  new RestTemplate();
    }

}
