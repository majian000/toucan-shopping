package com.toucan.shopping.starter.traceId.filter;

import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.modules.common.context.TraceContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * TraceId 过滤器 —— 优先复用上游传入的 X-Trace-Id，确保全链路同一 traceId
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(TraceIdFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        // 优先使用上游传入的 traceId
        String traceId = request.getHeader(TraceConstants.HTTP_HEADER);
        if (!StringUtils.hasText(traceId)) {
            traceId = UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, TraceConstants.TRACE_ID_LENGTH);
        }
        // 写入响应头，方便调用方获取
        response.setHeader(TraceConstants.HTTP_HEADER, traceId);
        MDC.put(TraceConstants.TRACE_ID_KEY, traceId);
        TraceContext.set(traceId);
        log.info("[TraceId] 设置完成: traceId={}, thread={}", traceId, Thread.currentThread().getName());
        try {
            filterChain.doFilter(request, response);
        } finally {
            log.info("[TraceId] 清理: traceId={}, thread={}", traceId, Thread.currentThread().getName());
            MDC.remove(TraceConstants.TRACE_ID_KEY);
            TraceContext.remove();
        }
    }
}
