package com.toucan.shopping.cloud.base.config.server.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心服务端
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableConfigServer
public class CloudConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudConfigServerApplication.class, args);
    }
}
