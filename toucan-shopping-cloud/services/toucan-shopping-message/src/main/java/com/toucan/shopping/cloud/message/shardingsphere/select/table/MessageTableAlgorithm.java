package com.toucan.shopping.cloud.message.shardingsphere.select.table;


import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * 默认自定义分表
 */
public class MessageTableAlgorithm /*implements StandardShardingAlgorithm<Date> */{

//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Override
//    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
//
//        Date createDate = shardingValue.getValue();
//        String year = String.format("%tY", createDate);
//        String month =String.valueOf(Integer.parseInt(String.format("%tm", createDate))); //去掉前面的0,如01,去掉后剩1
//
//        String targetTableName =shardingValue.getLogicTableName() + "_" + year+"_"+month;
//
//        for (String tableName : availableTargetNames) {
//            if (tableName.equals(targetTableName)) {
//                logger.info("表为：{}, 目标表为：{}, 最终被分到的表为：【{}】", availableTargetNames, targetTableName, tableName);
//                return tableName;
//            }
//        }
//        throw new IllegalArgumentException();
//    }
//
//    @Override
//    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
//        return null;
//    }
//
//    @Override
//    public void init() {
//
//    }
//
//    @Override
//    public String getType() {
//        return null;
//    }
}
