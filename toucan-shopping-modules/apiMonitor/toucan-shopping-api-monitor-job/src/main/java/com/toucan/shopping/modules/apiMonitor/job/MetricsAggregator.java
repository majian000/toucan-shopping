package com.toucan.shopping.modules.apiMonitor.job;

import com.toucan.shopping.modules.apiMonitor.entity.ApiMonitorMetricsPO;
import com.toucan.shopping.modules.apiMonitor.service.ApiMonitorMetricsService;
import com.toucan.shopping.modules.apiMonitor.service.ApiMonitorRecordService;
import com.toucan.shopping.modules.common.generator.IdGenerator;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 分钟聚合定时任务
 * <p>
 * 使用 Watermark + Java 侧聚合实现（兼容 ShardingSphere 分表）：
 * - 单表 SELECT GROUP BY 预聚合（ShardingSphere 允许单表查询）
 * - 查询已存在的 metrics keys 进行去重
 * - 逐批 INSERT 新聚合行（单表写入，ShardingSphere 按 sharding_date 路由）
 * - 启动时立即追赶遗漏数据
 */
@Component
public class MetricsAggregator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /** 安全边界：Watermark 回退 2 分钟 */
    private static final long WATERMARK_SAFE_MARGIN_MS = 2 * 60_000;

    /** 首次启动无 watermark 时最大回追 1 小时 */
    private static final long MAX_CATCH_UP_MS = 3600_000L;

    @Autowired
    private ApiMonitorRecordService apiMonitorRecordService;

    @Autowired
    private ApiMonitorMetricsService apiMonitorMetricsService;

    @Autowired
    private IdGenerator idGenerator;

    private final AtomicBoolean startupCatchUpDone = new AtomicBoolean(false);

    @PostConstruct
    public void startupCatchUp() {
        try {
            doAggregate();
            logger.info("[聚合] 启动追赶完成");
        } catch (Exception e) {
            logger.error("[聚合] 启动追赶失败: {}", e.getMessage(), e);
        } finally {
            startupCatchUpDone.set(true);
        }
    }

    @Scheduled(fixedDelay = 60_000)
    public void aggregate() {
        if (!startupCatchUpDone.get()) {
            return;
        }
        try {
            doAggregate();
        } catch (Exception e) {
            logger.error("[聚合] 聚合失败: {}", e.getMessage(), e);
        }
    }

    private void doAggregate() {
        Date to = new Date();
        Date watermark = apiMonitorMetricsService.selectMaxTimeWindow();

        // 无水位线 或 水位线为 epoch（空表），限制回追范围
        if (watermark == null || watermark.getTime() <= 0) {
            watermark = new Date(to.getTime() - MAX_CATCH_UP_MS);
        }

        Date from = new Date(watermark.getTime() - WATERMARK_SAFE_MARGIN_MS);

        // 1. 从 record 表预聚合（单表 GROUP BY，ShardingSphere 兼容）
        List<ApiMonitorMetricsPO> aggregated = apiMonitorRecordService.selectAggregated(from, to);
        if (aggregated.isEmpty()) {
            return;
        }

        // 2. 查询已存在的窗口 keys
        List<ApiMonitorMetricsPO> existing = apiMonitorMetricsService.selectExistingKeys(from, to);
        Set<String> existingKeys = existing.stream()
                .map(MetricsAggregator::keyOf)
                .collect(Collectors.toSet());

        // 3. 过滤已存在的，按 sharding_date 分组插入
        List<ApiMonitorMetricsPO> newRows = aggregated.stream()
                .filter(row -> !existingKeys.contains(keyOf(row)))
                .collect(Collectors.toList());

        if (!newRows.isEmpty()) {
            // 生成雪花ID
            for (ApiMonitorMetricsPO row : newRows) {
                row.setId(idGenerator.id());
            }
            int count = apiMonitorMetricsService.batchInsert(newRows);
            logger.debug("[聚合] 新增 {} 行 (from={}, to={})", count, from, to);
        }
    }

    /** 构造去重 key */
    static String keyOf(ApiMonitorMetricsPO row) {
        return row.getApiUrl() + "|" + row.getAppName() + "|"
                + (row.getTimeWindow() != null ? row.getTimeWindow().getTime() : 0);
    }
}
