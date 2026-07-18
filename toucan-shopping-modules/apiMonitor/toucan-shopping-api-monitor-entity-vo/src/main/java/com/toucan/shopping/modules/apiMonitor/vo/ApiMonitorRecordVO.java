package com.toucan.shopping.modules.apiMonitor.vo;

import lombok.Data;

/**
 * 接口监控上报记录
 */
@Data
public class ApiMonitorRecordVO {

    /** 接口URL */
    private String apiUrl;

    /** 请求方法 GET/POST/PUT/DELETE */
    private String method;

    /** 响应耗时(毫秒) */
    private long elapsedMs;

    /** HTTP响应状态码 200/404/500 */
    private int statusCode;

    /** 链路追踪ID，关联日志系统 */
    private String traceId;

    /** 应用名称，取自 spring.application.name */
    private String appName;

    /** 请求处理的服务器IP */
    private String serverIp;

    /** 请求时间 */
    private String requestTime;
}
