package com.toucan.shopping.modules.apiMonitor.service.impl;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorRecordPO;
import com.toucan.shopping.modules.apiMonitor.mapper.ApiMonitorRecordMapper;
import com.toucan.shopping.modules.apiMonitor.service.ApiMonitorRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ApiMonitorRecordServiceImpl implements ApiMonitorRecordService {

    @Autowired
    private ApiMonitorRecordMapper apiMonitorRecordMapper;

    @Override
    public int batchInsert(List<ApiMonitorRecordPO> records) {
        if (records == null || records.isEmpty()) {
            return 0;
        }
        return apiMonitorRecordMapper.batchInsert(records);
    }

    @Override
    public int aggregateToMetrics(Date startTime, Date endTime) {
        return apiMonitorRecordMapper.aggregateToMetrics(startTime, endTime);
    }

    @Override
    public List<ApiMonitorRecordPO> selectSlowList(String apiUrl, String appName, int minElapsedMs, Date startTime, Date endTime) {
        return apiMonitorRecordMapper.selectSlowList(apiUrl, appName, minElapsedMs, startTime, endTime);
    }
}
