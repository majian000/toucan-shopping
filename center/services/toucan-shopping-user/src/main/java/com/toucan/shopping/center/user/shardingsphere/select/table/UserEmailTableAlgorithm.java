package com.toucan.shopping.center.user.shardingsphere.select.table;


import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 自定义分表
 */
public class UserEmailTableAlgorithm implements PreciseShardingAlgorithm<String> {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public String doSharding(Collection<String> availableTargetNames, PreciseShardingValue<String> shardingValue) {
        for (String tableName : availableTargetNames) {
            //email的值 如果hash(email)%2==0 就去bbs_user_email_0 如果等于1 就去bbs_user_email_1
            if (tableName.endsWith((shardingValue.getValue().hashCode()&Integer.MAX_VALUE) % 2 + "")) {
                logger.info("表为：{}, 主键为：{}, 最终被分到的表为：【{}】", availableTargetNames, shardingValue, tableName);
                return tableName;
            }
        }
        throw new IllegalArgumentException();
    }
}
