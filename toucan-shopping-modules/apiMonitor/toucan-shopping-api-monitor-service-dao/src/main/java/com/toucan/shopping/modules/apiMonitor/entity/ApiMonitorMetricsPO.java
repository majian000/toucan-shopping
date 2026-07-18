package com.toucan.shopping.modules.apiMonitor.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 接口监控分钟聚合实体
 */
@Data
public class ApiMonitorMetricsPO {

    /** 主键ID */
    private Long id;

    /** 接口URL */
    private String apiUrl;

    /** 应用名称 */
    private String appName;

    /** 时间窗口(精确到分钟，如 16:32:00) */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date timeWindow;

    /** 该分钟内请求总数 */
    private Integer requestCount;

    /** 平均响应耗时(毫秒) */
    private BigDecimal avgMs;

    /** 最大响应耗时(毫秒) */
    private Integer maxMs;

    /** 最小响应耗时(毫秒) */
    private Integer minMs;

    /** 分片日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date shardingDate;

    /** 记录创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
}
