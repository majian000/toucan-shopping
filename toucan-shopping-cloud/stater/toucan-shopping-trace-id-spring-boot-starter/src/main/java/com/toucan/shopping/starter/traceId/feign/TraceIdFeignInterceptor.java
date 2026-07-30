package com.toucan.shopping.starter.traceId.feign;

import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.modules.common.context.TraceContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Feign 调用时自动将当前 traceId 写入请求头，透传给下游服务。
 * 优先从 MDC 读取（支持跨线程传播），MDC 为空时 fallback 到 TraceContext ThreadLocal。
 */
public class TraceIdFeignInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TraceIdFeignInterceptor.class);

    @Override
    public void apply(RequestTemplate template) {
        String traceId = MDC.get(TraceConstants.TRACE_ID_KEY);
        String traceCtx = TraceContext.get();
        log.info("[FeignTraceId] MDC={}, TraceContext={}, thread={}, url={}",
                traceId, traceCtx, Thread.currentThread().getName(), template.url());
        // MDC 优先（支持 Resilience4j ContextPropagator 跨线程搬运）
        if (traceId == null) {
            traceId = traceCtx;
        }
        if (traceId != null) {
            template.header(TraceConstants.HTTP_HEADER, traceId);
        }
    }
}
