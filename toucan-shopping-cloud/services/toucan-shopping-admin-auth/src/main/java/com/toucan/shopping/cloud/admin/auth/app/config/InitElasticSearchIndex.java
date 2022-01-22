package com.toucan.shopping.cloud.admin.auth.app.config;


import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.FunctionCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.common.properties.Toucan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 初始化es中索引
 */
@Configuration
public class InitElasticSearchIndex {


    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AdminRoleCacheService adminRoleCacheService;

    @Autowired
    private RoleFunctionCacheService roleFunctionCacheService;

    @Autowired
    private FunctionCacheService functionCacheService;

    @PostConstruct
    public void initIndex()
    {
        logger.info(" 初始化elasticsearch索引........");
        //初始化账号角色索引
        try {
            if (adminRoleCacheService != null) {
                if (!adminRoleCacheService.existsIndex()) {
                    adminRoleCacheService.createIndex();
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }

        //初始化角色功能项索引
        try {
            if (roleFunctionCacheService != null) {
                if (!roleFunctionCacheService.existsIndex()) {
                    roleFunctionCacheService.createIndex();
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }

        //初始化功能项索引
        try {
            if (functionCacheService != null) {
                if (!functionCacheService.existsIndex()) {
                    functionCacheService.createIndex();
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }


}
