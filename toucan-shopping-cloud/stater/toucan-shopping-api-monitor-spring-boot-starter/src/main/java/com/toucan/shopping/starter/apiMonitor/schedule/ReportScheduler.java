package com.toucan.shopping.starter.apiMonitor.schedule;

import com.toucan.shopping.cloud.apiMonitor.api.ApiMonitorReportServiceAPI;
import com.toucan.shopping.modules.apiMonitor.vo.ApiMonitorRecordVO;
import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.starter.apiMonitor.core.RecordCollector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 定时上报调度器 —— ApiMonitorReportServiceAPI 缺失时不阻塞启动，运行时按需从容器获取
 */
public class ReportScheduler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RecordCollector collector;

    @Autowired
    private Toucan toucan;

    @Autowired
    private ApplicationContext applicationContext;

    /** 注入失败时允许为空 */
    @Autowired(required = false)
    private ApiMonitorReportServiceAPI apiMonitorReportServiceAPI;

    /** 只打印一次异常日志，避免刷屏 */
    private final AtomicBoolean loggedMissing = new AtomicBoolean(false);

    @Scheduled(fixedDelayString = "#{toucan.plugins.apiMonitor.report.interval * 1000}")
    public void report() {
        try {
            ensureReportService();
            if (apiMonitorReportServiceAPI == null) {
                // 抛弃队列数据，避免内存堆积
                List<ApiMonitorRecordVO> discard = new ArrayList<>();
                collector.drainTo(discard, Integer.MAX_VALUE);
                if (!loggedMissing.getAndSet(true)) {
                    logger.warn("[MONITOR] ApiMonitorReportServiceAPI 不可用，监控数据暂不上报");
                }
                return;
            }
            loggedMissing.set(false);

            int batchSize = toucan.getPlugins().getApiMonitor().getReport().getBatchSize();
            List<ApiMonitorRecordVO> batch = new ArrayList<>(batchSize);
            collector.drainTo(batch, batchSize);

            if (!batch.isEmpty()) {
                apiMonitorReportServiceAPI.sendBatch(batch);
            }

            long dropped = collector.getAndResetDroppedCount();
            if (dropped > 0) {
                logger.warn("[MONITOR] 队列满，丢弃 {} 条记录", dropped);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
    }

    /** 运行时从容器获取，兜底注入失败的场景 */
    private void ensureReportService() {
        if (apiMonitorReportServiceAPI != null) {
            return;
        }
        apiMonitorReportServiceAPI = applicationContext.getBean(ApiMonitorReportServiceAPI.class);
        if (apiMonitorReportServiceAPI != null) {
            logger.info("[MONITOR] 运行时成功获取 ApiMonitorReportServiceAPI");
        }
    }
}
