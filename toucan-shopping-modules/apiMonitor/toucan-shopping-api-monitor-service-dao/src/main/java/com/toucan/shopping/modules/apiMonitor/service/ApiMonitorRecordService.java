package com.toucan.shopping.modules.apiMonitor.service;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorRecordPO;

import java.util.Date;
import java.util.List;

/**
 * 接口监控原始记录服务
 */
public interface ApiMonitorRecordService {

    /** 批量插入 */
    int batchInsert(List<ApiMonitorRecordPO> records);

    /** 聚合到分钟表 */
    int aggregateToMetrics(Date startTime, Date endTime);

    /** 查询慢请求列表 */
    List<ApiMonitorRecordPO> selectSlowList(String apiUrl, String appName, int minElapsedMs, Date startTime, Date endTime);
}
