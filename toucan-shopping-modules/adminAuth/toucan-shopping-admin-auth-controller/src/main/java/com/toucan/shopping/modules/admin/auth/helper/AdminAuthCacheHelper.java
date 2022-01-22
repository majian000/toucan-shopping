package com.toucan.shopping.modules.admin.auth.helper;

import com.toucan.shopping.modules.admin.auth.cache.service.AdminRoleCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.FunctionCacheService;
import com.toucan.shopping.modules.admin.auth.cache.service.RoleFunctionCacheService;

/**
 * 权限中台缓存助手
 */
public class AdminAuthCacheHelper {


    /**
     * 管理员角色缓存
     */
    private static AdminRoleCacheService adminRoleCacheService;


    /**
     * 功能项缓存
     */
    private static FunctionCacheService functionCacheService;

    /**
     * 角色功能项缓存
     */
    private static RoleFunctionCacheService roleFunctionCacheService;


    public static FunctionCacheService getFunctionCacheService() {
        return functionCacheService;
    }

    public static void setFunctionCacheService(FunctionCacheService functionCacheService) {
        AdminAuthCacheHelper.functionCacheService = functionCacheService;
    }

    public static RoleFunctionCacheService getRoleFunctionCacheService() {
        return roleFunctionCacheService;
    }

    public static void setRoleFunctionCacheService(RoleFunctionCacheService roleFunctionCacheService) {
        AdminAuthCacheHelper.roleFunctionCacheService = roleFunctionCacheService;
    }

    public static AdminRoleCacheService getAdminRoleCacheService() {
        return adminRoleCacheService;
    }

    public static void setAdminRoleCacheService(AdminRoleCacheService adminRoleCacheService) {
        AdminAuthCacheHelper.adminRoleCacheService = adminRoleCacheService;
    }
}
