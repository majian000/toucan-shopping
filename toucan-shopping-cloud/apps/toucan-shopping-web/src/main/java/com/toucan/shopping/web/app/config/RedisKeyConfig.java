package com.toucan.shopping.web.app.config;


import com.toucan.shopping.modules.common.properties.Toucan;
import com.toucan.shopping.web.redis.AreaRedisKey;
import com.toucan.shopping.web.redis.BannerRedisKey;
import com.toucan.shopping.web.redis.UserLoginRedisKey;
import com.toucan.shopping.web.redis.UserRegistRedisKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * redis key的应用编码替换
 */
@Configuration
public class RedisKeyConfig {

    @Autowired
    private Toucan toucan;


    @PostConstruct
    public void initAppCode()
    {
        UserRegistRedisKey.appCode = toucan.getAppCode();
        UserLoginRedisKey.appCode = toucan.getAppCode();
        AreaRedisKey.appCode = toucan.getAppCode();
        BannerRedisKey.appCode = toucan.getAppCode();
    }

}
