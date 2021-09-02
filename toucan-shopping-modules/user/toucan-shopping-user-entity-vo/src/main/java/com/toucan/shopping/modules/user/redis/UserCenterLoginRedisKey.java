package com.toucan.shopping.modules.user.redis;

/**
 * 用户登录相关
 */
public class UserCenterLoginRedisKey {



    public static Integer LOGIN_TIMEOUT_SECOND=60*60*5; //没有任何操作,5小时超时





    /**
     * 登录锁
     * @param mobile
     * @return
     */
    public static String getLoginLockKey(String mobile)
    {
        return "TOUCAN_SHOPPING_WEB:USER:LOCK:USER_LOGIN_"+mobile+"_LOCK";
    }


    /**
     * 判断是否实名锁
     * @param mobile
     * @return
     */
    public static String getVerifyRealNameLockKey(String mobile)
    {
        return "TOUCAN_SHOPPING_WEB:USER:LOCK:USER_VERIFY_REAL_NAME_"+mobile+"_LOCK";
    }




    /**
     * 登录用户组键
     * @param userId
     * @return
     */
    public static String getLoginInfoGroupKey(String userId)
    {
        return "TOUCAN_SHOPPING_WEB:USER:LOGIN_INFOS:"+userId+"_LOGIN_TOKENS";
    }


    /**
     * 登录信息键
     * @return
     */
    public static String getLoginTokenAppKey(String userId, String appCode)
    {
        return "TOUCAN_SHOPPING_WEB:USER:LOGIN:LOGIN_TOKEN_"+userId;
    }


    /**
     * 登录信息键
     * @return
     */
    public static String getLoginInfoAppKey(String userId, String appCode)
    {
        return "TOUCAN_SHOPPING_WEB:USER:LOGIN:LOGIN_INFO_"+userId;
    }
}
