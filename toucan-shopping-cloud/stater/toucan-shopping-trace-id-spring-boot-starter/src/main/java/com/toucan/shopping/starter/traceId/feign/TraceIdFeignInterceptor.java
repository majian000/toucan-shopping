package com.toucan.shopping.starter.traceId.feign;

import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.modules.common.context.TraceContext;
import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Feign 调用时自动将当前 traceId 写入请求头，透传给下游服务
 */
public class TraceIdFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate template) {
        String traceId = TraceContext.get();
        if (traceId != null) {
            template.header(TraceConstants.HTTP_HEADER, traceId);
        }
    }
}
