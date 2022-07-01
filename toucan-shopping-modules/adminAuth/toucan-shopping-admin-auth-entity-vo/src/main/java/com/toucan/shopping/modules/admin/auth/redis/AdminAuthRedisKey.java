package com.toucan.shopping.modules.admin.auth.redis;

public class AdminAuthRedisKey {


    public static Integer LOGIN_TIMEOUT_SECOND=60*60*60; //1小时超时



    /**
     * 登录token键
     * @param adminIdOrUserId
     * @return
     */
    public static String getLoginTokenGroupKey(String adminIdOrUserId)
    {
        return "TOUCAN_ADMIN_AUTH:LOGIN_TOKENS:"+adminIdOrUserId+"_LOGIN_TOKENS";
    }


    /**
     * 登录token app键
     * @return
     */
    public static String getLoginTokenAppKey(String adminIdOrUserId,String appCode)
    {
        return adminIdOrUserId+"_LOGIN_TOKENS_"+appCode;
    }

    public static String getAppRedisKey(String appCode)
    {
        return "TOUCAN_ADMIN_AUTH:APPS:"+appCode;
    }

}
