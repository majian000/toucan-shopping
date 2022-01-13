package com.toucan.shopping.cloud.apps.web.app.config;


import com.toucan.shopping.cloud.apps.web.redis.UserEditInfoRedisKey;
import com.toucan.shopping.modules.area.constant.BannerRedisKey;
import com.toucan.shopping.cloud.apps.web.redis.UserLoginRedisKey;
import com.toucan.shopping.cloud.apps.web.redis.UserRegistRedisKey;
import com.toucan.shopping.modules.common.context.ToucanApplicationContext;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 启动后参数替换
 */
@Configuration
public class AfterStartConfig {

    @Autowired
    private Toucan toucan;


    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;


    @PostConstruct
    public void initAppCode()
    {
        UserRegistRedisKey.appCode = toucan.getAppCode();
        UserLoginRedisKey.appCode = toucan.getAppCode();
        BannerRedisKey.appCode = toucan.getAppCode();
        UserEditInfoRedisKey.appCode = toucan.getAppCode();

        ToucanApplicationContext.setMaxFileSize(maxFileSize);
    }

}