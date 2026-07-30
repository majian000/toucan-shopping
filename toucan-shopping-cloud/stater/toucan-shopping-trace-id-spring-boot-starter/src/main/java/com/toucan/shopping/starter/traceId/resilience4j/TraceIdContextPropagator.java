package com.toucan.shopping.starter.traceId.resilience4j;

import com.toucan.shopping.modules.common.constant.TraceConstants;
import com.toucan.shopping.modules.common.context.TraceContext;
import io.github.resilience4j.core.ContextPropagator;
import org.slf4j.MDC;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Resilience4j 线程切换时自动搬运 TraceId 上下文。
 * 每次线程池/隔离线程创建时，从父线程抓取 traceId 并在子线程恢复。
 */
public class TraceIdContextPropagator implements ContextPropagator<String> {

    @Override
    public Supplier<Optional<String>> retrieve() {
        // 在父线程（调用方线程）抓取 traceId
        return () -> {
            String traceId = TraceContext.get();
            if (traceId != null) {
                return Optional.of(traceId);
            }
            String mdcTraceId = MDC.get(TraceConstants.TRACE_ID_KEY);
            return Optional.ofNullable(mdcTraceId);
        };
    }

    @Override
    public Consumer<Optional<String>> copy() {
        // 在子线程（Resilience4j 线程）恢复 traceId
        return optional -> optional.ifPresent(traceId -> {
            TraceContext.set(traceId);
            MDC.put(TraceConstants.TRACE_ID_KEY, traceId);
        });
    }

    @Override
    public Consumer<Optional<String>> clear() {
        // 子线程执行完毕后清理，防止线程池复用污染
        return optional -> {
            TraceContext.remove();
            MDC.remove(TraceConstants.TRACE_ID_KEY);
        };
    }
}
