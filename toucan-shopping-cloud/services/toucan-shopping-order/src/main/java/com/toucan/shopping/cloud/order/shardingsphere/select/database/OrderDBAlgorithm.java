package com.toucan.shopping.cloud.order.shardingsphere.select.database;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

/**
 * 自定义分库
 */
public class OrderDBAlgorithm implements PreciseShardingAlgorithm<Date> {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        Date createDate = shardingValue.getValue();
        String year = String.format("%tY", createDate);

        String targetDBName =shardingValue.getLogicTableName() + "_" + year;

        for (String dbName : availableTargetNames) {
            if (dbName.equals(targetDBName)) {
                logger.info("库为：{}, 目标库为：{}, 最终被分到的表为：【{}】", availableTargetNames, targetDBName, dbName);
                return dbName;
            }
        }
        throw new IllegalArgumentException();
    }
}
