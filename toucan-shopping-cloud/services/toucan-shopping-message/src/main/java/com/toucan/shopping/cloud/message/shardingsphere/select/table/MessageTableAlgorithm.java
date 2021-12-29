package com.toucan.shopping.cloud.message.shardingsphere.select.table;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;

/**
 * 默认自定义分表
 */
public class MessageTableAlgorithm implements PreciseShardingAlgorithm<Date> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {

        Date createDate = shardingValue.getValue();
        String year = String.format("%tY", createDate);
        String month =String.valueOf(Integer.parseInt(String.format("%tm", createDate))); //去掉前面的0,如01,去掉后剩1

        String targetTableName =shardingValue.getLogicTableName() + "_" + year+"_"+month;

        for (String tableName : availableTargetNames) {
            if (tableName.equals(targetTableName)) {
                logger.info("表为：{}, 目标表为：{}, 最终被分到的表为：【{}】", availableTargetNames, targetTableName, tableName);
                return tableName;
            }
        }
        throw new IllegalArgumentException();
    }
}
