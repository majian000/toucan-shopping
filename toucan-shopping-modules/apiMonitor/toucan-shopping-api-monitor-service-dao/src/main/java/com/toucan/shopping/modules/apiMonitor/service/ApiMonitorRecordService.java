package com.toucan.shopping.modules.apiMonitor.service;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorMetricsPO;
import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorRecordPO;

import java.util.Date;
import java.util.List;

/**
 * 接口监控原始记录服务
 */
public interface ApiMonitorRecordService {

    /** 批量插入 */
    int batchInsert(List<ApiMonitorRecordPO> records);

    /** 聚合到分钟表（已被 Java 侧聚合替代，保留用于兼容） */
    int aggregateToMetrics(Date startTime, Date endTime);

    /** 从 record 表 GROUP BY 预聚合（单表 SELECT，兼容 ShardingSphere） */
    List<ApiMonitorMetricsPO> selectAggregated(Date startTime, Date endTime);

    /** 查询慢请求列表 */
    List<ApiMonitorRecordPO> selectSlowList(String apiUrl, String appName, int minElapsedMs, Date startTime, Date endTime, Integer offset, Integer limit);

    /** 统计慢请求总数 */
    long countSlowList(String apiUrl, String appName, int minElapsedMs, Date startTime, Date endTime);
}
