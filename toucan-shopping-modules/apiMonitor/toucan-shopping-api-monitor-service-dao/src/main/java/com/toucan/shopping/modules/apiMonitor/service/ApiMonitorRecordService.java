package com.toucan.shopping.modules.apiMonitor.service;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorRecordPO;
import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorSummaryPO;

import java.util.Date;
import java.util.List;

/**
 * 接口监控原始记录服务
 */
public interface ApiMonitorRecordService {

    /** 批量插入 */
    int batchInsert(List<ApiMonitorRecordPO> records);

    /** 查询慢请求列表 */
    List<ApiMonitorRecordPO> selectSlowList(String apiUrl, String appName, int minElapsedMs, Date startTime, Date endTime, String traceId, Integer offset, Integer limit);

    /** 统计请求总数 */
    long countSlowList(String apiUrl, String appName, int minElapsedMs, Date startTime, Date endTime, String traceId);

    /** 概要统计（按接口+应用聚合） */
    List<ApiMonitorSummaryPO> selectSummary(String apiUrl, String appName, Date startTime, Date endTime, Integer minElapsed);
}
