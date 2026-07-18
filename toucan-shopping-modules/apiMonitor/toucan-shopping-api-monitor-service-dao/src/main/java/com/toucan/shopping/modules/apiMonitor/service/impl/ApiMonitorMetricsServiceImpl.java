package com.toucan.shopping.modules.apiMonitor.service.impl;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorMetricsPO;
import com.toucan.shopping.modules.apiMonitor.mapper.ApiMonitorMetricsMapper;
import com.toucan.shopping.modules.apiMonitor.service.ApiMonitorMetricsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ApiMonitorMetricsServiceImpl implements ApiMonitorMetricsService {

    @Autowired
    private ApiMonitorMetricsMapper apiMonitorMetricsMapper;

    @Override
    public List<ApiMonitorMetricsPO> selectSummary(String apiUrl, String appName, Date startTime, Date endTime) {
        return apiMonitorMetricsMapper.selectSummary(apiUrl, appName, startTime, endTime);
    }

    @Override
    public List<ApiMonitorMetricsPO> selectTrend(String apiUrl, String appName, Date startTime, Date endTime) {
        return apiMonitorMetricsMapper.selectTrend(apiUrl, appName, startTime, endTime);
    }

    @Override
    public Date selectMaxTimeWindow() {
        return apiMonitorMetricsMapper.selectMaxTimeWindow();
    }

    @Override
    public int deleteByCreateDate(Date beforeDate) {
        return apiMonitorMetricsMapper.deleteByCreateDate(beforeDate);
    }
}
