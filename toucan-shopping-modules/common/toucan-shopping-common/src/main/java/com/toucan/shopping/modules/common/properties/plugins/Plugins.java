package com.toucan.shopping.modules.common.properties.plugins;

import lombok.Data;

/**
 * 插件列表
 */
@Data
public class Plugins {

    /**
     * XSS过滤器
     */
    private XssFilter xssFilter;

    /**
     * 链路追踪
     */
    private TraceId traceId;

    /**
     * 接口监控
     */
    private ApiMonitor apiMonitor;


}
