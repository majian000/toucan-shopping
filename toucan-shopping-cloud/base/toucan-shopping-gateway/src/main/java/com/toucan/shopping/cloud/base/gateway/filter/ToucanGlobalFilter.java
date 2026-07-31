package com.toucan.shopping.cloud.base.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器 —— 从请求头解析 traceId 打印网关日志。
 */
@Component
public class ToucanGlobalFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("route request {} traceId={}",
                exchange.getRequest().getPath(), resolveTraceId(exchange));
        return chain.filter(exchange);
    }

    private String resolveTraceId(ServerWebExchange exchange) {
        String b3 = exchange.getRequest().getHeaders().getFirst("b3");
        if (StringUtils.hasText(b3)) {
            int dash = b3.indexOf('-');
            return dash > 0 ? b3.substring(0, dash) : b3;
        }
        String b3TraceId = exchange.getRequest().getHeaders().getFirst("X-B3-TraceId");
        if (StringUtils.hasText(b3TraceId)) {
            return b3TraceId;
        }
        return exchange.getRequest().getHeaders().getFirst("X-Trace-Id");
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
