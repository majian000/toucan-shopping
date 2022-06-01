package com.toucan.shopping.cloud.db.sharding.database;


import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Date;
import java.util.Properties;

/**
 * 自定义分库
 */
public class DBDateYearAlgorithm implements StandardShardingAlgorithm<Date> {


    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Properties props = new Properties();
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
        return null;
    }

    @Override
    public void init() {
        this.instanceName = String.valueOf(props.get("instance-name")!=null?props.get("instance-name"):"");
    }

    @Override
    public String getType() {
        return "DBDateYearAlgorithm";
    }


    @Override
    public Properties getProps() {
        return props;
    }

    @Override
    public void setProps(Properties properties) {
        this.props=properties;
    }
}
