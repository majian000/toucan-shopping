package com.toucan.shopping.modules.admin.auth.helper;

import com.toucan.shopping.modules.admin.auth.cache.service.*;

/**
 * 权限中台缓存助手
 */
public class AdminAuthCacheHelper {

    private static AdminLoginCacheService adminLoginCacheService;
    private static AdminRoleCacheService adminRoleCacheService;
    private static FunctionCacheService functionCacheService;
    private static RoleFunctionCacheService roleFunctionCacheService;
    private static AppCacheService appCacheService;

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

    public static AppCacheService getAppCacheService() {
        return appCacheService;
    }

    public static void setAppCacheService(AppCacheService appCacheService) {
        AdminAuthCacheHelper.appCacheService = appCacheService;
    }

    public static AdminLoginCacheService getAdminLoginCacheService() {
        return adminLoginCacheService;
    }

    public static void setAdminLoginCacheService(AdminLoginCacheService adminLoginCacheService) {
        AdminAuthCacheHelper.adminLoginCacheService = adminLoginCacheService;
    }
}
