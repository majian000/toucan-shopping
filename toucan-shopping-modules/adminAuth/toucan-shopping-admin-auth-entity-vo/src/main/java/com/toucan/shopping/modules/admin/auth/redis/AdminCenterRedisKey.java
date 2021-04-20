package com.toucan.shopping.modules.admin.auth.redis;

public class AdminCenterRedisKey {


    public static Integer LOGIN_TIMEOUT_SECOND=60*60*60; //1小时超时



    /**
     * 登录token键
     * @param adminIdOrUserId
     * @return
     */
    public static String getLoginTokenGroupKey(String adminIdOrUserId)
    {
        return adminIdOrUserId+"_LOGIN_TOKENS";
    }


    /**
     * 登录token app键
     * @return
     */
    public static String getLoginTokenAppKey(String adminIdOrUserId,String appCode)
    {
        return adminIdOrUserId+"_LOGIN_TOKENS_"+appCode;
    }

}
