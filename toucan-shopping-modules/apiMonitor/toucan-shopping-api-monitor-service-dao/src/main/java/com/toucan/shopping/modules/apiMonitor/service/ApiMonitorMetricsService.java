package com.toucan.shopping.modules.apiMonitor.service;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorMetricsPO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 接口监控分钟聚合服务
 */
public interface ApiMonitorMetricsService {

    /** 概要统计 */
    List<ApiMonitorMetricsPO> selectSummary(String apiUrl, String appName, Date startTime, Date endTime);

    /** 趋势查询 */
    List<ApiMonitorMetricsPO> selectTrend(String apiUrl, String appName, Date startTime, Date endTime);

    /** 删除过期数据 */
    int deleteByCreateDate(Date beforeDate);
}
