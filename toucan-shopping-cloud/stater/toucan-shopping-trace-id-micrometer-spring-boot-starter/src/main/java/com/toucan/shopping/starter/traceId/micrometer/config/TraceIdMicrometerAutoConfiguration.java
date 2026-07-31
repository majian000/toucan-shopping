package com.toucan.shopping.starter.traceId.micrometer.config;

import com.toucan.shopping.starter.traceId.micrometer.support.TraceContextBridgeFilter;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Micrometer Tracing 自动配置。
 * 将 Micrometer Span 的 traceId 桥接到 {@code TraceContext} ThreadLocal，
 * B3 传播、Feign 注入、MDC 写入均由 Micrometer 标准机制完成。
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "toucan.plugins.traceId", name = "enabled", havingValue = "true")
public class TraceIdMicrometerAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(TraceIdMicrometerAutoConfiguration.class);

    @Bean
    TraceContextBridgeFilter traceContextBridgeFilter(Tracer tracer) {
        log.info("[TraceId-Micrometer] TraceContextBridgeFilter 注册完成");
        return new TraceContextBridgeFilter(tracer);
    }
}
