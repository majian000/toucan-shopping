package com.toucan.shopping.cloud.db.sharding.database;


import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 自定义分库
 */
public class DBLongAlgorithm implements StandardShardingAlgorithm<Long> {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String instanceName;
    private int num;



    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        for (String dbName : availableTargetNames) {
            //分库策略 如果hash(id)%2==0 就去db_0 如果等于1 就是db_1
            if (dbName.endsWith(((shardingValue.getValue().hashCode()&Integer.MAX_VALUE) % num) + "")) {
                logger.info(instanceName+" 库为：{}, 主键为：{}, 最终被分到的库为：【{}】", availableTargetNames, shardingValue, dbName);
                return dbName;
            }
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Long> rangeShardingValue) {
        return collection;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }
    @Override
    public String getType() {
        return getClass().getSimpleName();
    }
}
