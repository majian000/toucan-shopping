package com.toucan.shopping.starter.traceId.micrometer.support;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;

/**
 * 从 Micrometer Tracing 获取当前 traceId。
 * 替代旧的 TraceContext ThreadLocal，跨线程自动传播。
 */
public class TraceIdSupplier {

    private final Tracer tracer;

    public TraceIdSupplier(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * 获取当前 Span 的 traceId，当前没有 Span 则返回 null
     */
    public String get() {
        Span span = tracer.currentSpan();
        return span != null ? span.context().traceId() : null;
    }
}
