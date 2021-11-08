package com.toucan.shopping.cloud.order.shardingsphere.select.table;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 默认自定义分表
 */
public class DefaultTableAlgorithm implements PreciseShardingAlgorithm<Long> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        for (String tableName : availableTargetNames) {
            if (tableName.endsWith(shardingValue.getValue() % 2 + "")) {
                logger.info("表为：{}, 主键为：{}, 最终被分到的表为：【{}】", availableTargetNames, shardingValue, tableName);
                return tableName;
            }
        }
        throw new IllegalArgumentException();
    }
}
