package com.toucan.shopping.cloud.admin.auth.app.config;


import com.toucan.shopping.modules.admin.auth.es.service.AdminRoleElasticSearchService;
import com.toucan.shopping.modules.admin.auth.es.service.FunctionElasticSearchService;
import com.toucan.shopping.modules.admin.auth.es.service.RoleFunctionElasticSearchService;
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
    private AdminRoleElasticSearchService adminRoleElasticSearchService;

    @Autowired
    private RoleFunctionElasticSearchService roleFunctionElasticSearchService;

    @Autowired
    private FunctionElasticSearchService functionElasticSearchService;

    @PostConstruct
    public void initIndex()
    {
        logger.info(" 初始化elasticsearch索引........");
        //初始化账号角色索引
        try {
            if (adminRoleElasticSearchService != null) {
                if (!adminRoleElasticSearchService.existsIndex()) {
                    adminRoleElasticSearchService.createIndex();
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }

        //初始化角色功能项索引
        try {
            if (roleFunctionElasticSearchService != null) {
                if (!roleFunctionElasticSearchService.existsIndex()) {
                    roleFunctionElasticSearchService.createIndex();
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }

        //初始化功能项索引
        try {
            if (functionElasticSearchService != null) {
                if (!functionElasticSearchService.existsIndex()) {
                    functionElasticSearchService.createIndex();
                }
            }
        }catch(Exception e)
        {
            logger.warn(e.getMessage(),e);
        }
    }


}
