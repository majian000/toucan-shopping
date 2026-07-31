package com.toucan.shopping.starter.traceId.micrometer.support;

import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.modules.common.context.TraceContext;
import io.micrometer.tracing.Tracer;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 将 Micrometer 的 traceId 同步到 TraceContext，方便业务代码扩展
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
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
        try {
            var span = tracer.currentSpan();
            if (span != null) {
                String traceId = span.context().traceId();
                TraceContext.set(traceId);
            } else {
                // 没有 Span 时从 MDC 兜底（兼容非 web 线程）
                String traceId = MDC.get(TraceConstants.TRACE_ID_KEY);
                if (traceId != null) {
                    TraceContext.set(traceId);
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            TraceContext.remove();
        }
    }
}
