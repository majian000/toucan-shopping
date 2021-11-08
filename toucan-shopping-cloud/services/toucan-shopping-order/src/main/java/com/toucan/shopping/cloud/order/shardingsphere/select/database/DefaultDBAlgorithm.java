package com.toucan.shopping.cloud.order.shardingsphere.select.database;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 自定义分库
 */
public class DefaultDBAlgorithm implements PreciseShardingAlgorithm<Long> {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        for (String dbName : availableTargetNames) {
            //分库策略 如果hash(id)%2==0 就去db_0 如果等于1 就是db_1
            if (dbName.endsWith(((shardingValue.getValue().hashCode()&Integer.MAX_VALUE) % 3) + "")) {
                logger.info("库为：{}, 主键为：{}, 最终被分到的库为：【{}】", availableTargetNames, shardingValue, dbName);
                return dbName;
            }
        }
        throw new IllegalArgumentException();
    }
}
