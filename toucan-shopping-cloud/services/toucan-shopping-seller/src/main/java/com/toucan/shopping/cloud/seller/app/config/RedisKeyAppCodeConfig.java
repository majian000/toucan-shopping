package com.toucan.shopping.cloud.seller.app.config;


import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 配置静态类中应用编码
 */
@Configuration
public class RedisKeyAppCodeConfig {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private Toucan toucan;

    @PostConstruct
    public void initAppCode()
    {

    }


}
