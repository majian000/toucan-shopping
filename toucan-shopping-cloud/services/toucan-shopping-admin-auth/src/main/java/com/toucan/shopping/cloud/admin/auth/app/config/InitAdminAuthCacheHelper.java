package com.toucan.shopping.cloud.admin.auth.app.config;

import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.FunctionCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.common.spring.context.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Order(value = 1)
public class InitAdminAuthCacheHelper implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void run(ApplicationArguments args)  {
        logger.info("初始化权限中台缓存服务.....");
//        AdminAuthCacheHelper.setAdminRoleCacheService(SpringContextHolder.getBean(AdminRoleCacheService.class));
//        AdminAuthCacheHelper.setFunctionCacheService(SpringContextHolder.getBean(FunctionCacheService.class));
//        AdminAuthCacheHelper.setRoleFunctionCacheService(SpringContextHolder.getBean(RoleFunctionCacheService.class));

        logger.info("权限中台缓存服务 adminRoleCacheService : {}",AdminAuthCacheHelper.getAdminRoleCacheService());
        logger.info("权限中台缓存服务 functionCacheService : {}",AdminAuthCacheHelper.getFunctionCacheService());
        logger.info("权限中台缓存服务 roleFunctionCacheService : {}",AdminAuthCacheHelper.getRoleFunctionCacheService());
    }
}
