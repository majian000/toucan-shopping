package com.toucan.shopping.modules.apiMonitor.service;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorMetricsPO;
import com.toucan.shopping.modules.apiMonitor.service.ApiMonitorMetricsService;
import com.toucan.shopping.modules.apiMonitor.service.ApiMonitorRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Dashboard 查询服务
 */
@Service
public class DashboardService {

    @Autowired
    private ApiMonitorMetricsService apiMonitorMetricsService;

    @Autowired
    private ApiMonitorRecordService apiMonitorRecordService;

    /** 概要统计（Java 侧分页，metrics 聚合表数据量小） */
    public Map<String, Object> getSummary(String apiUrl, String appName, int minutes, int page, int limit) {
        Date endTime = new Date();
        Date startTime = new Date(endTime.getTime() - (long) minutes * 60_000);

        List<ApiMonitorMetricsPO> list = apiMonitorMetricsService.selectSummary(
                apiUrl, appName, startTime, endTime);
        int total = list.size();
        int fromIndex = (page - 1) * limit;
        int toIndex = Math.min(fromIndex + limit, total);
        List<ApiMonitorMetricsPO> pageItems = list.subList(
                Math.min(fromIndex, total), toIndex);

        Map<String, Object> result = new HashMap<>();
        result.put("items", pageItems);
        result.put("total", total);
        return result;
    }

    /** 趋势查询（Java 侧分页，metrics 聚合表数据量小） */
    public Map<String, Object> getTrend(String apiUrl, String appName, int range, int page, int limit) {
        Date endTime = new Date();
        Date startTime = new Date(endTime.getTime() - (long) range * 60_000);

        List<ApiMonitorMetricsPO> list = apiMonitorMetricsService.selectTrend(
                apiUrl, appName, startTime, endTime);
        int total = list.size();
        int fromIndex = (page - 1) * limit;
        int toIndex = Math.min(fromIndex + limit, total);
        List<ApiMonitorMetricsPO> pageItems = list.subList(
                Math.min(fromIndex, total), toIndex);

        Map<String, Object> result = new HashMap<>();
        result.put("items", pageItems);
        result.put("total", total);
        return result;
    }

    /** 慢请求列表（SQL 侧分页，record 表数据量大） */
    public Map<String, Object> getSlowList(String apiUrl, String appName, int minElapsed, int page, int size) {
        Date endTime = new Date();
        Date startTime = new Date(endTime.getTime() - 3600_000L);

        int offset = (page - 1) * size;
        List<?> items = apiMonitorRecordService.selectSlowList(
                apiUrl, appName, minElapsed, startTime, endTime, offset, size);
        long total = apiMonitorRecordService.countSlowList(
                apiUrl, appName, minElapsed, startTime, endTime);

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("total", total);
        return result;
    }
}
