package com.toucan.shopping.cloud.user;

import com.toucan.shopping.modules.kafka.config.KafkaCustomerConfig;
import com.toucan.shopping.modules.kafka.config.KafkaProducerConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients(basePackages = "com.toucan.shopping")
@MapperScan({"com.toucan.shopping.modules.user.mapper",
        "com.toucan.shopping.modules.common.persistence.mapper"})
@ComponentScan(basePackages = {"com.toucan.shopping"},
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = KafkaCustomerConfig.class),
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = KafkaProducerConfig.class)})
public class CloudUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudUserServiceApplication.class, args);
    }
}
