package com.toucan.shopping.starter.traceId.filter;

import com.toucan.shopping.modules.common.constant.TraceConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * TraceId 过滤器
 * 每个请求生成唯一 traceId，注入 MDC，日志自动带上
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String traceId = UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0, TraceConstants.TRACE_ID_LENGTH);
            MDC.put(TraceConstants.TRACE_ID_KEY, traceId);
            request.setAttribute(TraceConstants.TRACE_ID_ATTR, traceId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TraceConstants.TRACE_ID_KEY);
        }
    }
}
