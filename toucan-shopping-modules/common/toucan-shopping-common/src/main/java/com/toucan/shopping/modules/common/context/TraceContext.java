package com.toucan.shopping.modules.common.context;

/**
 * TraceId 上下文（ThreadLocal）
 */
public final class TraceContext {

    private static final ThreadLocal<String> TRACE_ID_HOLDER = new ThreadLocal<>();

    private TraceContext() {}

    public static void set(String traceId) {
        TRACE_ID_HOLDER.set(traceId);
    }

    public static String get() {
        return TRACE_ID_HOLDER.get();
    }

    public static void remove() {
        TRACE_ID_HOLDER.remove();
    }
}
