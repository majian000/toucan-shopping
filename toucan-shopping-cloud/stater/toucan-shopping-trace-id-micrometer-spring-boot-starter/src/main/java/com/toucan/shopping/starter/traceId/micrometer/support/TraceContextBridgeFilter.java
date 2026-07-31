package com.toucan.shopping.starter.traceId.micrometer.support;

import brave.Tracing;
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
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * 将 traceId 同步到 {@link TraceContext} 并确保 Brave Span 使用正确的 traceId。
 * <p>
 * 在 Brave TracingFilter（+5）之前执行，如果请求头有 X-Trace-Id，
 * 则用它创建 Brave Span，确保后续 Feign 调用和 Micrometer 自动传播
 * 都使用上游透传的 traceId，不会在既是接收者又是调用者时错乱。
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 4) // 必须在 Brave TracingFilter (+5) 之前
public class TraceContextBridgeFilter extends OncePerRequestFilter {

    private final Tracer micrometerTracer;
    private final brave.Tracer braveTracer;

    public TraceContextBridgeFilter(Tracer micrometerTracer, Tracing tracing) {
        this.micrometerTracer = micrometerTracer;
        this.braveTracer = tracing.tracer();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        brave.Span span = tryJoinIncomingTrace(request);
        try {
            // 从 Span 或请求头同步到 TraceContext
            TraceContext.set(resolveTraceId(request));
            filterChain.doFilter(request, response);
        } finally {
            TraceContext.remove();
            if (span != null) {
                span.finish();
            }
        }
    }

    /**
     * 如果请求头有 X-Trace-Id，用它的 traceId 创建一个 Brave Span 作为当前 Span。
     * 这样无论是 Micrometer 的 MDC 写入还是 Feign 的出站传播，都用同一个 traceId。
     */
    private brave.Span tryJoinIncomingTrace(HttpServletRequest request) {
        String headerTraceId = request.getHeader(TraceConstants.HTTP_HEADER);
        if (!StringUtils.hasText(headerTraceId)) {
            return null;
        }
        try {
            long traceIdHigh = 0;
            long traceIdLow = 0;
            if (headerTraceId.length() == 32) {
                traceIdHigh = Long.parseUnsignedLong(headerTraceId.substring(0, 16), 16);
                traceIdLow = Long.parseUnsignedLong(headerTraceId.substring(16), 16);
            } else if (headerTraceId.length() == 16) {
                traceIdLow = Long.parseUnsignedLong(headerTraceId, 16);
            } else {
                return null;
            }
            // traceId(long) 是 low 位，traceIdHigh(long) 是 high 位
            brave.propagation.TraceContext context = brave.propagation.TraceContext.newBuilder()
                    .traceIdHigh(traceIdHigh)
                    .traceId(traceIdLow)
                    .spanId(UUID.randomUUID().getLeastSignificantBits())
                    .sampled(true)
                    .build();
            return braveTracer.joinSpan(context).name("incoming").start();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Span（已用 header traceId 创建或 Brave 自己创建） → 请求头 → MDC → UUID
     */
    private String resolveTraceId(HttpServletRequest request) {
        // 1. Micrometer Span：此时 Brave TracingFilter 可能已创建子 Span，traceId 相同
        var span = micrometerTracer.currentSpan();
        if (span != null) {
            return span.context().traceId();
        }
        // 2. 请求头兜底
        String traceId = request.getHeader(TraceConstants.HTTP_HEADER);
        if (StringUtils.hasText(traceId)) {
            return traceId;
        }
        // 3. MDC 兜底
        traceId = MDC.get(TraceConstants.TRACE_ID_KEY);
        if (StringUtils.hasText(traceId)) {
            return traceId;
        }
        // 4. UUID 最终兜底
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
}
