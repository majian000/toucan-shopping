package com.toucan.shopping.starter.traceId.micrometer.support;

import brave.Tracing;
import brave.propagation.Propagation;
import brave.propagation.TraceContextOrSamplingFlags;
import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.modules.common.context.TraceContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;

/**
 * 将上游透传的 traceId 注入 Brave Span，确保整个调用链 traceId 唯一。
 * <p>
 * 在 Brave TracingFilter（+5）之前，用 X-Trace-Id 请求头创建父 Span，
 * Brave 会在此基础上创建子 Span，traceId 始终保持一致。
 * 同时同步到 {@link TraceContext}，供业务代码使用。
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 4) // 必须在 Brave TracingFilter (+5) 之前
public class TraceContextBridgeFilter extends OncePerRequestFilter {

    private final brave.Tracer braveTracer;

    public TraceContextBridgeFilter(Tracing tracing) {
        this.braveTracer = tracing.tracer();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        brave.Span span = createParentSpan(request);
        try {
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
     * 用 X-Trace-Id 请求头创建父 Span，
     * 借助 Brave B3 Propagation 解析 hex traceId，无需手动拆 high/low。
     * Brave TracingFilter（+5）在此基础上创建子 Span，traceId 保持不变。
     */
    private brave.Span createParentSpan(HttpServletRequest request) {
        String traceId = request.getHeader(TraceConstants.HTTP_HEADER);
        if (!StringUtils.hasText(traceId)) {
            return null;
        }
        try {
            LinkedHashMap<String, String> carrier = new LinkedHashMap<>();
            carrier.put("X-B3-TraceId", traceId);
            carrier.put("X-B3-SpanId", Long.toHexString(UUID.randomUUID().getLeastSignificantBits()));
            carrier.put("X-B3-Sampled", "1");
            TraceContextOrSamplingFlags flags = Propagation.B3_STRING
                    .extractor((c, k) -> carrier.get(k))
                    .extract(carrier);
            brave.propagation.TraceContext context = flags.context();
            if (context != null) {
                return braveTracer.newChild(context).name("incoming").start();
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    private String resolveTraceId(HttpServletRequest request) {
        String traceId = request.getHeader(TraceConstants.HTTP_HEADER);
        if (StringUtils.hasText(traceId)) {
            return traceId;
        }
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }
}
