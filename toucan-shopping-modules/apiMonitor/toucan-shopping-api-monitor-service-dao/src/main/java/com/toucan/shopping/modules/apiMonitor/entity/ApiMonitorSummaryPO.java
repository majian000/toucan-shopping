package com.toucan.shopping.modules.apiMonitor.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 接口监控概要统计实体（直接由原始记录表 GROUP BY 得出）
 */
@Data
public class ApiMonitorSummaryPO {

    /** 接口URL */
    private String apiUrl;

    /** 应用名称 */
    private String appName;

    /** 请求总数 */
    private Long requestCount;

    /** 平均响应耗时(毫秒) */
    private BigDecimal avgMs;

    /** 最大响应耗时(毫秒) */
    private Integer maxMs;

    /** 最小响应耗时(毫秒) */
    private Integer minMs;
}
