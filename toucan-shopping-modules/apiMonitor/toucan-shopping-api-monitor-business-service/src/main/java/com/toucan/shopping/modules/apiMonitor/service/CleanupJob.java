package com.toucan.shopping.modules.apiMonitor.service;

import com.toucan.shopping.modules.apiMonitor.mapper.ApiMonitorMetricsMapper;
import com.toucan.shopping.modules.apiMonitor.mapper.ApiMonitorRecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * 数据清理定时任务
 */
@Component
public class CleanupJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApiMonitorRecordMapper apiMonitorRecordMapper;

    @Autowired
    private ApiMonitorMetricsMapper apiMonitorMetricsMapper;

    /** 每天凌晨3点清理过期数据 */
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanup() {
        try {
            Calendar cal = Calendar.getInstance();

            // record 保留7天
            cal.add(Calendar.DAY_OF_MONTH, -7);
            Date recordBefore = cal.getTime();
            int recordCount = apiMonitorRecordMapper.deleteByCreateDate(recordBefore);
            logger.info("清理 {} 条过期 record 数据", recordCount);

            // metrics 保留30天
            cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, -30);
            Date metricsBefore = cal.getTime();
            int metricsCount = apiMonitorMetricsMapper.deleteByCreateDate(metricsBefore);
            logger.info("清理 {} 条过期 metrics 数据", metricsCount);
        } catch (Exception e) {
            logger.error("清理过期数据失败: {}", e.getMessage());
        }
    }
}
