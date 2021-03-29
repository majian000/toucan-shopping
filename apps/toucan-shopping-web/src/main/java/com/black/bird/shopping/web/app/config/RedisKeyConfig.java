package com.toucan.shopping.web.app.config;


import com.toucan.shopping.common.properties.BlackBird;
import com.toucan.shopping.lock.redis.RedisLock;
import com.toucan.shopping.lock.redis.thread.RedisLockManagerThread;
import com.toucan.shopping.web.redis.AreaRedisKey;
import com.toucan.shopping.web.redis.BannerRedisKey;
import com.toucan.shopping.web.redis.UserLoginRedisKey;
import com.toucan.shopping.web.redis.UserRegistRedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;

/**
 * redis key的应用编码替换
 */
@Configuration
public class RedisKeyConfig {

    @Autowired
    private BlackBird blackBird;


    @PostConstruct
    public void initAppCode()
    {
        UserRegistRedisKey.appCode = blackBird.getAppCode();
        UserLoginRedisKey.appCode = blackBird.getAppCode();
        AreaRedisKey.appCode = blackBird.getAppCode();
        BannerRedisKey.appCode = blackBird.getAppCode();
    }

}
