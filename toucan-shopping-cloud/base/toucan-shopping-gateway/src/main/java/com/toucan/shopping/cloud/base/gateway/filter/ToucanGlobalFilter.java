package com.toucan.shopping.cloud.base.gateway.filter;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 全局过滤器 —— 将 Micrometer 当前 Span 的 traceId 写入 MDC，方便日志检索。
 * B3 传播由 Micrometer 的 ObservationWebFilter（入站）+ instrumented HttpClient（出站）自动完成，
 * 无需手动处理请求头。
 */
@Component
public class ToucanGlobalFilter implements GlobalFilter, Ordered {

    private static final String TRACE_ID_KEY = "traceId";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Tracer tracer;

    public ToucanGlobalFilter(Tracer tracer) {
        this.tracer = tracer;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return Mono.deferContextual(ctx -> {
            Span span = tracer.currentSpan();
            if (span != null) {
                MDC.put(TRACE_ID_KEY, span.context().traceId());
            }
            logger.debug("route request {} traceId={}", exchange.getRequest().getPath(),
                    span != null ? span.context().traceId() : "N/A");
            return chain.filter(exchange)
                    .doFinally(s -> MDC.remove(TRACE_ID_KEY));
        });
    }

    @Override
    public int getOrder() {
        return -100;
    }
}
