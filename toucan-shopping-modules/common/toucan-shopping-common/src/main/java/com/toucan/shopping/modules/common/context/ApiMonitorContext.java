package com.toucan.shopping.modules.common.context;

/**
 * 接口监控上下文（ThreadLocal）
 */
public final class ApiMonitorContext {

    private static final ThreadLocal<Long> START_NANOS = new ThreadLocal<>();
    private static final ThreadLocal<String> PATTERN = new ThreadLocal<>();

    private ApiMonitorContext() {}

    public static void setStartNanos(long nanos) {
        START_NANOS.set(nanos);
    }

    public static Long getStartNanos() {
        return START_NANOS.get();
    }

    public static void setPattern(String pattern) {
        PATTERN.set(pattern);
    }

    public static String getPattern() {
        return PATTERN.get();
    }

    public static void remove() {
        START_NANOS.remove();
        PATTERN.remove();
    }
}
