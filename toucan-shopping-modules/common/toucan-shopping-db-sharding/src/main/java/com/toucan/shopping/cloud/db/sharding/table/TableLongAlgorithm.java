package com.toucan.shopping.cloud.db.sharding.table;


import org.apache.shardingsphere.sharding.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.RangeShardingValue;
import org.apache.shardingsphere.sharding.api.sharding.standard.StandardShardingAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Properties;

/**
 * 默认自定义分表
 */
public class TableLongAlgorithm implements StandardShardingAlgorithm<Long> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Properties props = new Properties();
    private String instanceName;
    private int num;


    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<Long> shardingValue) {
        for (String tableName : availableTargetNames) {
            if (tableName.endsWith(shardingValue.getValue() % num + "")) {
                logger.info("表为：{}, 主键为：{}, 最终被分到的表为：【{}】", availableTargetNames, shardingValue, tableName);
                return tableName;
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
        this.instanceName = String.valueOf(props.get("instance-name")!=null?props.get("instance-name"):"");
        this.num = Integer.parseInt(String.valueOf(props.get("num")!=null?props.get("num"):"0"));
        if(this.num==0)
        {
            throw new IllegalArgumentException("请配置分库数量");
        }
    }

    @Override
    public String getType() {
        return "TableLongAlgorithm";
    }


    @Override
    public Properties getProps() {
        return props;
    }

    @Override
    public void setProps(Properties properties) {
        this.props=properties;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
