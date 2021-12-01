package com.toucan.shopping.cloud.apps.web.redis;

/**
 * 找回密码
 */
public class UserForgetPasswordRedisKey {


    /**
     * 找回密码锁键
     * @param username
     * @return
     */
    public static String getForgetPasswordLockKey(String username)
    {
        return "TOUCAN_SHOPPING_WEB:FORGET:PASSWORD:LOCKS:"+username+"_lock";
    }





}
