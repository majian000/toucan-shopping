package com.toucan.shopping.starter.traceId.feign;

import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.modules.common.context.TraceContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;

/**
 * Feign 调用时自动将当前 traceId 写入请求头，透传给下游服务。
 * 优先从 MDC 读取（支持 Resilience4j 跨线程传播），
 * MDC 为空时 fallback 到 TraceContext ThreadLocal。
 */
public class TraceIdFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String traceId = MDC.get(TraceConstants.TRACE_ID_KEY);
        if (traceId == null) {
            traceId = TraceContext.get();
        }
        if (traceId != null) {
            template.header(TraceConstants.HTTP_HEADER, traceId);
        }
    }
}
