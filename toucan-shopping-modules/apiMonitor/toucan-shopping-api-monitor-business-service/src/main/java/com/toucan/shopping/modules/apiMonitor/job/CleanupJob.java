package com.toucan.shopping.modules.apiMonitor.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据清理定时任务
 */
@Component
public class CleanupJob {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /** 每天凌晨3点清理过期数据 */
    @Scheduled(cron = "0 0 3 * * ?")
    public void cleanup() {
        try {

            // record 保留7天，TRUNCATE 过期表
            truncateDayTable("api_monitor_record", 7);

            // metrics 保留30天，TRUNCATE 过期表
            truncateDayTable("api_monitor_metrics", 30);

        } catch (Exception e) {
            logger.error("清理过期数据失败: {}", e.getMessage());
        }
    }

    private void truncateDayTable(String tablePrefix, int retentionDays) {
        // 算出当前保留期内的活跃表号，清空其余所有表
        Set<Integer> activeDays = new HashSet<>();
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < retentionDays; i++) {
            activeDays.add(cal.get(Calendar.DAY_OF_MONTH));
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        for (int day = 1; day <= 31; day++) {
            if (activeDays.contains(day)) {
                continue;
            }
            String tableName = tablePrefix + "_" + day;
            try {
                jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
            } catch (Exception e) {
                logger.warn("清理表 {} 失败: {}", tableName, e.getMessage());
            }
        }
    }
}
