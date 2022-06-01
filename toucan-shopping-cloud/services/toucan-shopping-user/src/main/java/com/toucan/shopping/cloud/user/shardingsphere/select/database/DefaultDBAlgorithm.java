package com.toucan.shopping.cloud.user.shardingsphere.select.database;


import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Properties;

/**
 * 自定义分库
 */
public class DefaultDBAlgorithm implements StandardShardingAlgorithm<Long> {


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

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public String getType() {
        return null;
    }

    @Override
    public Properties getProps() {
        return null;
    }

    @Override
    public void setProps(Properties properties) {

    }
}
