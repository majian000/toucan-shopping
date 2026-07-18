package com.toucan.shopping.modules.apiMonitor.job;

import com.toucan.shopping.modules.apiMonitor.service.ApiMonitorMetricsService;
import com.toucan.shopping.modules.apiMonitor.service.ApiMonitorRecordService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 分钟聚合定时任务
 * <p>
 * 使用 Watermark 机制实现故障恢复：
 * - 每次聚合前查询 api_monitor_metrics 表中已处理的最新时间窗口
 * - 从该时间窗口开始追赶，聚合到当前时间
 * - INSERT IGNORE 保证重复聚合幂等
 * - 启动时立即执行一次追赶，补偿停机期间的遗漏数据
 */
@Component
public class MetricsAggregator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** 安全边界：Watermark 回退 2 分钟，避免因时间对齐问题漏数据 */
    private static final long WATERMARK_SAFE_MARGIN_MS = 2 * 60_000;

    @Autowired
    private ApiMonitorRecordService apiMonitorRecordService;

    @Autowired
    private ApiMonitorMetricsService apiMonitorMetricsService;

    private final AtomicBoolean startupCatchUpDone = new AtomicBoolean(false);

    /**
     * 启动时追赶停机期间的遗漏数据
     */
    @PostConstruct
    public void startupCatchUp() {
        try {
            Date watermark = apiMonitorMetricsService.selectMaxTimeWindow();
            if (watermark == null) {
                watermark = new Date(System.currentTimeMillis() - 3600_000L); // 首次启动追1小时
            }
            // 回退安全边界
            Date from = new Date(watermark.getTime() - WATERMARK_SAFE_MARGIN_MS);
            Date to = new Date();

            logger.info("[聚合] 启动追赶: from={}, to={}", from, to);
            int count = apiMonitorRecordService.aggregateToMetrics(from, to);
            logger.info("[聚合] 启动追赶完成: {} 条记录", count);
        } catch (Exception e) {
            logger.error("[聚合] 启动追赶失败: {}", e.getMessage(), e);
        } finally {
            startupCatchUpDone.set(true);
        }
    }

    /**
     * 每 60 秒从上次 watermark 追赶聚合
     */
    @Scheduled(fixedDelay = 60_000)
    public void aggregate() {
        if (!startupCatchUpDone.get()) {
            logger.debug("[聚合] 启动追赶尚未完成，跳过本次调度");
            return;
        }
        try {
            Date watermark = apiMonitorMetricsService.selectMaxTimeWindow();
            if (watermark == null) {
                watermark = new Date(System.currentTimeMillis() - 3600_000L);
            }

            Date from = new Date(watermark.getTime() - WATERMARK_SAFE_MARGIN_MS);
            Date to = new Date();

            int count = apiMonitorRecordService.aggregateToMetrics(from, to);
            if (count > 0) {
                logger.debug("[聚合] 聚合 {} 条记录到 metrics 表 (from={}, to={})", count, from, to);
            }
        } catch (Exception e) {
            logger.error("[聚合] 聚合失败: {}", e.getMessage(), e);
        }
    }
}
