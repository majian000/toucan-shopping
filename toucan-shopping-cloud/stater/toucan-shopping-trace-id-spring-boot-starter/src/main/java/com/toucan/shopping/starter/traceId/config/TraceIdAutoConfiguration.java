package com.toucan.shopping.starter.traceId.config;

import com.toucan.shopping.starter.traceId.feign.TraceIdFeignInterceptor;
import com.toucan.shopping.starter.traceId.filter.TraceIdFilter;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * TraceId 自动配置 — 确保在 Feign 客户端创建之前注册拦截器
 */
@AutoConfiguration
@AutoConfigureBefore(FeignAutoConfiguration.class)
@ConditionalOnProperty(prefix = "toucan.plugins.traceId", name = "enabled", havingValue = "true")
public class TraceIdAutoConfiguration {

    @Bean
    public TraceIdFilter traceIdFilter() {
        return new TraceIdFilter();
    }

    @Bean
    public TraceIdFeignInterceptor traceIdFeignInterceptor() {
        return new TraceIdFeignInterceptor();
    }
}
