package com.toucan.shopping.starter.traceId.config;

import com.toucan.shopping.starter.traceId.feign.TraceIdFeignInterceptor;
import com.toucan.shopping.starter.traceId.filter.TraceIdFilter;
import com.toucan.shopping.starter.traceId.resilience4j.TraceIdContextPropagator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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

    /**
     * Feign 拦截器配置 —— 放在内部静态类中，
     * @ConditionalOnClass 在类级别阻止加载，避免主配置类触发 NoClassDefFoundError
     */
    @AutoConfiguration
    @ConditionalOnClass(name = "feign.RequestInterceptor")
    static class FeignTraceIdConfiguration {

        @Bean
        public TraceIdFeignInterceptor traceIdFeignInterceptor() {
            return new TraceIdFeignInterceptor();
        }
    }

    /**
     * Resilience4j 上下文传播器 —— 线程池/隔离线程切换时自动搬运 TraceId，
     * 避免 ThreadLocal 跨线程丢失导致 traceId 透传中断
     */
    @AutoConfiguration
    @ConditionalOnClass(name = "io.github.resilience4j.core.ContextPropagator")
    static class Resilience4jTraceIdConfiguration {

        @Bean
        public TraceIdContextPropagator traceIdContextPropagator() {
            return new TraceIdContextPropagator();
        }
    }
}
