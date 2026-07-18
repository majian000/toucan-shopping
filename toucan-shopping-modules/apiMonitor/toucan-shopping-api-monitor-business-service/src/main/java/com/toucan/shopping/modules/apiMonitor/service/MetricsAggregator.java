package com.toucan.shopping.modules.apiMonitor.service;

import com.toucan.shopping.modules.apiMonitor.mapper.ApiMonitorRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 分钟聚合定时任务
 */
@Component
public class MetricsAggregator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiMonitorRecordMapper apiMonitorRecordMapper;

    /** 每60秒聚合一次 */
    @Scheduled(fixedDelay = 60_000)
    public void aggregate() {
        try {
            Date endTime = new Date();
            Date startTime = new Date(endTime.getTime() - 60_000);
            int count = apiMonitorRecordMapper.aggregateToMetrics(startTime, endTime);
            if (count > 0) {
                logger.debug("聚合 {} 条记录到 metrics 表", count);
            }
        } catch (Exception e) {
            logger.error("聚合 metrics 失败: {}", e.getMessage());
        }
    }
}
