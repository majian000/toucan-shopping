package com.toucan.shopping.starter.traceId.config;

import com.toucan.shopping.starter.traceId.filter.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * TraceId 自动配置
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "toucan.plugins.traceId", name = "enabled", havingValue = "true")
public class TraceIdAutoConfiguration {

    @Bean
    public TraceIdFilter traceIdFilter() {
        return new TraceIdFilter();
    }
}
