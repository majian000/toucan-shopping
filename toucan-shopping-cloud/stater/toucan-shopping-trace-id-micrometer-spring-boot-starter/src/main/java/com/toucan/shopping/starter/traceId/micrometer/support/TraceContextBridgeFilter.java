package com.toucan.shopping.starter.traceId.micrometer.support;

import com.toucan.shopping.modules.common.context.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 将 Micrometer Span 的 traceId 同步到 {@link TraceContext} ThreadLocal，
 * 供业务代码通过 {@code TraceContext.get()} 获取。
 * <p>
 * 入站 B3 解析、出站 Feign 传播均由 Micrometer Tracing 标准机制完成，本 Filter 只做桥接。
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class TraceContextBridgeFilter extends OncePerRequestFilter {

    private final Tracer tracer;

    public TraceContextBridgeFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        var span = tracer.currentSpan();
        if (span != null) {
            TraceContext.set(span.context().traceId());
        }
        try {
            filterChain.doFilter(request, response);
        } finally {
            TraceContext.remove();
        }
    }
}
