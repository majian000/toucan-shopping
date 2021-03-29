package com.toucan.shopping.center.user.app.config;


import com.toucan.shopping.center.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.center.user.redis.UserCenterRegistRedisKey;
import com.toucan.shopping.center.user.redis.UserCenterSendRegistSmsRedisKey;
import com.toucan.shopping.center.user.redis.UserCenterUserCacheRedisKey;
import com.toucan.shopping.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 锁管理器线程启动
 */
@Configuration
public class RedisKeyAppCodeConfig {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @PostConstruct
    public void initAppCode()
    {
        UserCenterLoginRedisKey.appCode= toucan.getAppCode();
        UserCenterRegistRedisKey.appCode= toucan.getAppCode();
        UserCenterSendRegistSmsRedisKey.appCode= toucan.getAppCode();
        UserCenterUserCacheRedisKey.appCode= toucan.getAppCode();

    }


}
