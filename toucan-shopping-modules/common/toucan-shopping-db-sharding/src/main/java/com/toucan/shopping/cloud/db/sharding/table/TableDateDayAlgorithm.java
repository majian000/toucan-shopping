package com.toucan.shopping.cloud.db.sharding.table;

import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

/**
 * 按天分表算法（月中第几天）
 * 表名格式: 逻辑表名_天数 (如 api_monitor_record_18)
 * 31 张固定表循环使用，每天 TRUNCATE 7 天前的表
 */
public class TableDateDayAlgorithm implements StandardShardingAlgorithm<Date> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String instanceName;

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        Date date = shardingValue.getValue();
        String day = String.valueOf(Integer.parseInt(String.format("%td", date))); // 月中第几天，去掉前导0
        String targetTableName = shardingValue.getLogicTableName() + "_" + day;

        for (String tableName : availableTargetNames) {
            if (tableName.equals(targetTableName)) {
                logger.debug("{} 目标表: {}", instanceName, tableName);
                return tableName;
            }
        }
        throw new IllegalArgumentException("未找到目标表: " + targetTableName);
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
        return collection;
    }

    @Override
    public String getType() {
        return getClass().getSimpleName();
    }
}
