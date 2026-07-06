package com.toucan.shopping.cloud.apps.admin.auth.scheduler.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackages = "com.toucan.shopping")
@Configuration
public class EnableFeignConfig {

    /**
     * Feign 客户端饥饿加载：启动时初始化所有 @FeignClient 代理对象，
     * 避免首次调用时的延迟和潜在的启动期错误遗漏
     */
    @Bean
    public ApplicationRunner feignEagerLoadRunner(ApplicationContext applicationContext) {
        return args -> {
            applicationContext.getBeansWithAnnotation(FeignClient.class);
        };
    }

}
