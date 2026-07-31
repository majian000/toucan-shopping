package com.toucan.shopping.cloud.base.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 全局过滤器 —— 透传上游 traceId。
 * <p>
 * 优先从请求头读取：B3 单头（b3）、B3 多头（X-B3-TraceId）、自定义（X-Trace-Id）。
 * 上游服务通过 {@code feign-micrometer} 自动注入 B3 头，
 * 网关只做透传，不依赖 Micrometer Span API，简单可靠。
 * 都没有则生成 UUID。
 */
@Component
public class ToucanGlobalFilter implements GlobalFilter, Ordered {

    private static final String TRACE_ID_KEY = "traceId";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = resolveTraceId(exchange);
        MDC.put(TRACE_ID_KEY, traceId);
        try {
            // 只写 X-Trace-Id，不碰 B3 头，避免与 instrumented HttpClient 注入冲突
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("X-Trace-Id", traceId)
                    .build();
            logger.debug("route request {} traceId={}", request.getPath(), traceId);
            return chain.filter(exchange.mutate().request(request).build());
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }
    }

    /**
     * 按优先级解析 traceId：
     * 1. b3 单头格式  →  取第一段（traceId）
     * 2. X-B3-TraceId 多头格式
     * 3. X-Trace-Id 自定义头
     * 4. UUID 兜底
     */
    private String resolveTraceId(ServerWebExchange exchange) {
        // 1. b3 单头：{traceId}-{spanId}-{sampled}
        String b3 = exchange.getRequest().getHeaders().getFirst("b3");
        if (StringUtils.hasText(b3)) {
            int dash = b3.indexOf('-');
            if (dash > 0) {
                return b3.substring(0, dash);
            }
            // 没有 '-' 则整个值就是 traceId
            return b3;
        }
        // 2. X-B3-TraceId 多头
        String b3TraceId = exchange.getRequest().getHeaders().getFirst("X-B3-TraceId");
        if (StringUtils.hasText(b3TraceId)) {
            return b3TraceId;
        }
        // 3. 自定义 X-Trace-Id
        String xTraceId = exchange.getRequest().getHeaders().getFirst("X-Trace-Id");
        if (StringUtils.hasText(xTraceId)) {
            return xTraceId;
        }
        // 4. UUID 兜底
        return UUID.randomUUID().toString().replace("-", "").substring(0, 32);
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
