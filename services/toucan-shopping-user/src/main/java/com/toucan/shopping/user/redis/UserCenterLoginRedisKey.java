package com.toucan.shopping.user.redis;

public class UserCenterLoginRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001002";


    public static Integer LOGIN_TIMEOUT_SECOND=60*60; //没有任何操作,1小时超时





    /**
     * 登录锁
     * @param mobile
     * @return
     */
    public static String getLoginLockKey(String mobile)
    {
        return appCode +"_USER_LOGIN_"+mobile+"_LOCK";
    }






    /**
     * 登录token键
     * @param userId
     * @return
     */
    public static String getLoginTokenGroupKey(String userId)
    {
        return userId+"_LOGIN_TOKENS";
    }


    /**
     * 登录token app键
     * @return
     */
    public static String getLoginTokenAppKey(String userId,String appCode)
    {
        return appCode+"_LOGIN_TOKEN_"+userId;
    }


}
