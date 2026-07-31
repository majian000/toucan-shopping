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
 * 全局过滤器 —— 透传上游 traceId，没有则生成，优先读 B3 头（Micrometer 标准）
 */
@Component
public class ToucanGlobalFilter implements GlobalFilter, Ordered {

    private static final String TRACE_ID_KEY = "traceId";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 优先 Micrometer B3 头，其次自定义 X-Trace-Id，都没有才生成
        String traceId = exchange.getRequest().getHeaders().getFirst("X-B3-TraceId");
        if (!StringUtils.hasText(traceId)) {
            traceId = exchange.getRequest().getHeaders().getFirst("X-Trace-Id");
        }
        if (!StringUtils.hasText(traceId)) {
            traceId = UUID.randomUUID().toString().replace("-", "").substring(0, 32);
        }
        MDC.put(TRACE_ID_KEY, traceId);
        try {
            ServerHttpRequest request = exchange.getRequest().mutate()
                    .header("X-Trace-Id", traceId)
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
