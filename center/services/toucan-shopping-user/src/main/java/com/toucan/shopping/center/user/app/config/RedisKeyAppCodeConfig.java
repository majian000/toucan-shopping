package com.toucan.shopping.center.user.app.config;


import com.toucan.shopping.center.user.redis.UserCenterLoginRedisKey;
import com.toucan.shopping.center.user.redis.UserCenterRegistRedisKey;
import com.toucan.shopping.center.user.redis.UserCenterSendRegistSmsRedisKey;
import com.toucan.shopping.center.user.redis.UserCenterUserCacheRedisKey;
import com.toucan.shopping.common.properties.BlackBird;
import com.toucan.shopping.lock.redis.RedisLock;
import com.toucan.shopping.lock.redis.thread.RedisLockManagerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;

/**
 * 锁管理器线程启动
 */
@Configuration
public class RedisKeyAppCodeConfig {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BlackBird blackBird;

    @PostConstruct
    public void initAppCode()
    {
        UserCenterLoginRedisKey.appCode=blackBird.getAppCode();
        UserCenterRegistRedisKey.appCode=blackBird.getAppCode();
        UserCenterSendRegistSmsRedisKey.appCode=blackBird.getAppCode();
        UserCenterUserCacheRedisKey.appCode=blackBird.getAppCode();

    }


}
