package com.toucan.shopping.modules.common.constant;

/**
 * 链路追踪常量
 */
public final class TraceConstants {

    private TraceConstants() {}

    /** MDC key / 日志字段名 */
    public static final String TRACE_ID_KEY = "traceId";

    /** HTTP 请求/响应 Header 名称 */
    public static final String HTTP_HEADER = "X-Trace-Id";

    /** traceId 默认长度（Micrometer Tracing 128-bit → 32 位十六进制） */
    public static final int TRACE_ID_LENGTH = 32;
}
