package com.toucan.shopping.modules.apiMonitor.service;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorMetricsPO;

import java.util.Date;
import java.util.List;

/**
 * 接口监控分钟聚合服务
 */
public interface ApiMonitorMetricsService {

    /** 概要统计 */
    List<ApiMonitorMetricsPO> selectSummary(String apiUrl, String appName, Date startTime, Date endTime);

    /** 趋势查询 */
    List<ApiMonitorMetricsPO> selectTrend(String apiUrl, String appName, Date startTime, Date endTime);

    /** 查询已聚合的最新时间窗口（ShardingSphere 分表，Service 层取各分片的最大值），用于补偿追赶 */
    Date selectMaxTimeWindow();

    /** 删除过期数据 */
    int deleteByCreateDate(Date beforeDate);
}
