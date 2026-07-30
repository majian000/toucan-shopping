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
 * 全局过滤器 —— 生成或透传 traceId，确保全链路可追踪
 */
@Component
public class ToucanGlobalFilter implements GlobalFilter, Ordered {

    private static final String TRACE_HEADER = "X-Trace-Id";
    private static final String TRACE_ID_KEY = "traceId";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 优先使用上游传入的 traceId
        String traceId = exchange.getRequest().getHeaders().getFirst(TRACE_HEADER);
        if (!StringUtils.hasText(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        }
        MDC.put(TRACE_ID_KEY, traceId);
        try {
            // 将 traceId 注入到下游请求头
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header(TRACE_HEADER, traceId)
                    .build();
            logger.debug("route request {} traceId={}", request.getPath(), traceId);
            return chain.filter(exchange.mutate().request(request).build());
        } finally {
            MDC.remove(TRACE_ID_KEY);
        }
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
