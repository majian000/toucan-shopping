package com.toucan.shopping.modules.common.constant;

/**
 * 链路追踪常量
 */
public final class TraceConstants {

    private TraceConstants() {}

    /** MDC key / HTTP Header key / 日志字段名 */
    public static final String TRACE_ID_KEY = "traceId";

    /** 请求 Attribute key */
    public static final String TRACE_ID_ATTR = "_trace_id";

    /** traceId 默认长度 */
    public static final int TRACE_ID_LENGTH = 8;
}
