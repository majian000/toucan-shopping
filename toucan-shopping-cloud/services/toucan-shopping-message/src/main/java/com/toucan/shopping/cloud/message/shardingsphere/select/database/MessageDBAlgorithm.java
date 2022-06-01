package com.toucan.shopping.cloud.message.shardingsphere.select.database;


import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;

/**
 * 自定义分库
 */
public class MessageDBAlgorithm /*implements StandardShardingAlgorithm<Date>*/ {


//    private final Logger logger = LoggerFactory.getLogger(getClass());
//
//    @Override
//    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Date> shardingValue) {
//        Date createDate = shardingValue.getValue();
//        String year = String.format("%tY", createDate);
//
//
//        for (String dbName : availableTargetNames) {
//            if (dbName.endsWith(year)) {
//                logger.info("库为：{}, 目标库后缀为：{}, 最终被分到的库为：【{}】", availableTargetNames, year, dbName);
//                return dbName;
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
