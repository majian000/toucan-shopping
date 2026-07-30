package com.toucan.shopping.starter.traceId.config;

import com.toucan.shopping.starter.traceId.resilience4j.TraceIdContextPropagator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Resilience4j TraceId 上下文传播自动配置。
 * 作为顶级 auto-configuration 类注册，确保 ContextPropagator Bean
 * 在 Spring Cloud 的 Resilience4JCircuitBreakerFactory 创建之前注册。
 */
@AutoConfiguration
@AutoConfigureBefore(name = "org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JAutoConfiguration")
@ConditionalOnClass(name = "io.github.resilience4j.core.ContextPropagator")
@ConditionalOnProperty(prefix = "toucan.plugins.traceId", name = "enabled", havingValue = "true")
public class TraceIdResilience4jAutoConfiguration {

    @Bean
    public TraceIdContextPropagator traceIdContextPropagator() {
        return new TraceIdContextPropagator();
    }
}
