package com.toucan.shopping.modules.apiMonitor.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 接口监控原始记录实体
 */
@Data
public class ApiMonitorRecordPO {

    /** 主键ID */
    private Long id;

    /** 接口URL */
    private String apiUrl;

    /** 请求方法 GET/POST/PUT/DELETE */
    private String httpMethod;

    /** 响应耗时(毫秒) */
    private Integer elapsedMs;

    /** HTTP响应状态码 200/404/500 */
    private Integer statusCode;

    /** 链路追踪ID，关联日志系统 */
    private String traceId;

    /** 应用名称 */
    private String appName;

    /** 请求处理服务器IP */
    private String serverIp;

    /** 请求时间(毫秒精度) */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date requestTime;

    /** 分片日期 */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date shardingDate;

    /** 记录创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;
}
