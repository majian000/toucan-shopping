package com.toucan.shopping.cloud.admin.auth.app.config;

import com.toucan.shopping.modules.admin.auth.cache.es.service.impl.AdminRoleElasticSearchServiceImpl;
import com.toucan.shopping.modules.admin.auth.cache.es.service.impl.FunctionElasticSearchServiceImpl;
import com.toucan.shopping.modules.admin.auth.cache.es.service.impl.RoleFunctionElasticSearchServiceImpl;
import com.toucan.shopping.modules.admin.auth.cache.redis.service.impl.AdminLoginCacheRedisServiceImpl;
import com.toucan.shopping.modules.admin.auth.cache.redis.service.impl.AppCacheRedisServiceImpl;
import com.toucan.shopping.modules.admin.auth.helper.AdminAuthCacheHelper;
import com.toucan.shopping.modules.common.spring.context.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class InitAdminAuthCacheHelper implements ApplicationRunner {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Override
    public void run(ApplicationArguments args)  {
        logger.info("初始化权限中台缓存服务.....");
        AdminAuthCacheHelper.setAdminRoleCacheService(SpringContextHolder.getBean(AdminRoleElasticSearchServiceImpl.class));
        AdminAuthCacheHelper.setFunctionCacheService(SpringContextHolder.getBean(FunctionElasticSearchServiceImpl.class));
        AdminAuthCacheHelper.setRoleFunctionCacheService(SpringContextHolder.getBean(RoleFunctionElasticSearchServiceImpl.class));
        AdminAuthCacheHelper.setAppCacheService(SpringContextHolder.getBean(AppCacheRedisServiceImpl.class));
        AdminAuthCacheHelper.setAdminLoginCacheService(SpringContextHolder.getBean(AdminLoginCacheRedisServiceImpl.class));

        logger.info("权限中台缓存服务 adminRoleCacheService : {}",AdminAuthCacheHelper.getAdminRoleCacheService());
        logger.info("权限中台缓存服务 functionCacheService : {}",AdminAuthCacheHelper.getFunctionCacheService());
        logger.info("权限中台缓存服务 roleFunctionCacheService : {}",AdminAuthCacheHelper.getRoleFunctionCacheService());
        logger.info("权限中台缓存服务 appCacheRedisService : {}",AdminAuthCacheHelper.getAppCacheService());
        logger.info("权限中台缓存服务 appCacheRedisService : {}",AdminAuthCacheHelper.getAdminLoginCacheService());
    }
}
