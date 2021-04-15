package com.toucan.shopping.standard.base.config.server.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * 配置中心服务端
 */

@SpringBootApplication
@EnableConfigServer
public class StandardConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(StandardConfigServerApplication.class, args);
    }
}
