package com.toucan.shopping.starter.apiMonitor.schedule;

import com.toucan.shopping.cloud.apiMonitor.api.cloud.feign.service.FeignApiMonitorService;
import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.starter.apiMonitor.core.RecordCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;

/**
 * 定时上报调度器
 */
public class ReportScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RecordCollector collector;

    @Autowired
    private FeignApiMonitorService feignClient;

    @Autowired
    private Toucan toucan;

    @Scheduled(fixedDelayString = "#{toucan.plugins.apiMonitor.report.interval * 1000}")
    public void report() {
        try {
            List<ApiMonitorRecordVO> batch = new ArrayList<>();
            int batchSize = toucan.getPlugins().getApiMonitor().getReport().getBatchSize();
            collector.drainTo(batch, batchSize);

            if (!batch.isEmpty()) {
                feignClient.sendBatch(batch);
            }

            // 每分钟打印丢弃计数
            long dropped = collector.getAndResetDroppedCount();
            if (dropped > 0) {
                logger.warn("[MONITOR] 队列满，丢弃 {} 条记录", dropped);
            }
        } catch (Exception e) {
            // 上报失败静默，不影响业务
        }
    }
}
