package com.toucan.shopping.starter.traceId.micrometer.support;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 从 Micrometer Tracing 获取当前 traceId。
 * 替代旧的 TraceContext ThreadLocal，跨线程自动传播。
 */
public class TraceIdSupplier {

    private static final Logger log = LoggerFactory.getLogger(TraceIdSupplier.class);

    private final Tracer tracer;

    public TraceIdSupplier(Tracer tracer) {
        this.tracer = tracer;
        log.info("[TraceId-Micrometer] TraceIdSupplier 创建完成, tracer={}", tracer.getClass().getSimpleName());
    }

    /**
     * 获取当前 Span 的 traceId，当前没有 Span 则返回 null
     */
    public String get() {
        Span span = tracer.currentSpan();
        if (span != null) {
            String traceId = span.context().traceId();
            log.info("[TraceId-Micrometer] 获取 traceId={}, spanId={}, thread={}",
                    traceId, span.context().spanId(), Thread.currentThread().getName());
            return traceId;
        }
        log.info("[TraceId-Micrometer] 当前无 Span, thread={}", Thread.currentThread().getName());
        return null;
    }
}
