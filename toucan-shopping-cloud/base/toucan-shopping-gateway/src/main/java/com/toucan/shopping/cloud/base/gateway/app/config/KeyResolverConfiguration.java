package com.toucan.shopping.cloud.base.gateway.app.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class KeyResolverConfiguration {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    /**
     * 基于路径
     * @return
     */
    @Bean
    public KeyResolver pathKeyResolver(){
        logger.info("初始化令牌桶限流Key对象........");
        return exchange -> Mono.just(
                exchange.getRequest().getPath().toString());
    }

}
