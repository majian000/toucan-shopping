package com.toucan.shopping.cloud.db.sharding.database;


import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

/**
 * 自定义分库
 */
public class DBDateYearAlgorithm implements StandardShardingAlgorithm<Date> {


    private final Logger logger = LoggerFactory.getLogger(getClass());
    private String instanceName;


    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
        Date createDate = shardingValue.getValue();
        String year = String.format("%tY", createDate);


        for (String dbName : availableTargetNames) {
            if (dbName.endsWith(year)) {
                logger.info(instanceName+"库为：{}, 目标库后缀为：{}, 最终被分到的库为：【{}】", availableTargetNames, year, dbName);
                return dbName;
            }
        }
        throw new IllegalArgumentException();
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
