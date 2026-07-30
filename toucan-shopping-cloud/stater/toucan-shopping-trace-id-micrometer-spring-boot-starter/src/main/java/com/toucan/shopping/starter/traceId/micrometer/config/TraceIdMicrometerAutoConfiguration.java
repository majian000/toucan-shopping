package com.toucan.shopping.starter.traceId.micrometer.config;

import brave.baggage.BaggageField;
import brave.baggage.BaggagePropagation;
import brave.baggage.BaggagePropagationConfig;
import brave.propagation.B3Propagation;
import brave.propagation.Propagation;
import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.starter.traceId.micrometer.support.TraceIdSupplier;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;

/**
 * Micrometer Tracing 自动配置 ——
 * 1. 注册 X-Trace-Id 作为额外传播字段（兼容旧版调用方）
 * 2. 提供 {@link TraceIdSupplier} 替换旧的 {@code TraceContext} ThreadLocal
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "toucan.plugins.traceId", name = "enabled", havingValue = "true")
public class TraceIdMicrometerAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(TraceIdMicrometerAutoConfiguration.class);

    /**
     * 创建 BaggageField 保存 X-Trace-Id 值，跨服务传播兼容旧版调用方
     */
    @Bean
    BaggageField xTraceIdBaggageField() {
        return BaggageField.create(TraceConstants.HTTP_HEADER);
    }

    /**
     * 将 X-Trace-Id 注册为远程传播字段。
     * 入站：从 X-Trace-Id 请求头读取
     * 出站：自动写入 X-Trace-Id 响应头（与 B3 头并存）
     */
    @Bean
    Propagation.Factory propagationFactory(BaggageField xTraceIdBaggageField) {
        log.info("[TraceId-Micrometer] 注册 X-Trace-Id 额外传播字段");
        return BaggagePropagation.newFactoryBuilder(B3Propagation.FACTORY)
                .add(BaggagePropagationConfig.SingleBaggageField.remote(xTraceIdBaggageField))
                .build();
    }

    /**
     * 提供 TraceIdSupplier Bean，替换旧的 TraceContext.get()。
     */
    @Bean
    TraceIdSupplier traceIdSupplier(Tracer tracer) {
        log.info("[TraceId-Micrometer] TraceIdSupplier 注册完成");
        return new TraceIdSupplier(tracer);
    }
}
