package com.toucan.shopping.modules.apiMonitor.service;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorMetricsPO;
import com.toucan.shopping.modules.apiMonitor.mapper.ApiMonitorMetricsMapper;
import com.toucan.shopping.modules.apiMonitor.mapper.ApiMonitorRecordMapper;
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
    private ApiMonitorMetricsMapper apiMonitorMetricsMapper;

    @Autowired
    private ApiMonitorRecordMapper apiMonitorRecordMapper;

    /** 概要统计 */
    public Map<String, Object> getSummary(String apiUrl, String appName, int minutes) {
        Date endTime = new Date();
        Date startTime = new Date(endTime.getTime() - (long) minutes * 60_000);

        List<ApiMonitorMetricsPO> list = apiMonitorMetricsMapper.selectSummary(
                apiUrl, appName, startTime, endTime);

        Map<String, Object> result = new HashMap<>();
        result.put("items", list);
        result.put("time", new Date());
        return result;
    }

    /** 趋势查询 */
    public List<ApiMonitorMetricsPO> getTrend(String apiUrl, String appName, int range) {
        Date endTime = new Date();
        Date startTime = new Date(endTime.getTime() - (long) range * 60_000);

        return apiMonitorMetricsMapper.selectTrend(apiUrl, appName, startTime, endTime);
    }

    /** 慢请求列表 */
    public List<?> getSlowList(String apiUrl, String appName, int minElapsed, int page, int size) {
        Date endTime = new Date();
        Date startTime = new Date(endTime.getTime() - 3600_000L); // 最近1小时

        return apiMonitorRecordMapper.selectSlowList(
                apiUrl, appName, minElapsed, startTime, endTime);
    }
}
