package com.toucan.shopping.cloud.apps.web.app.config;


import com.toucan.shopping.cloud.apps.web.redis.AreaRedisKey;
import com.toucan.shopping.modules.area.constant.BannerRedisKey;
import com.toucan.shopping.cloud.apps.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.apps.web.redis.UserRegistRedisKey;
import com.toucan.shopping.modules.common.properties.Toucan;
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
