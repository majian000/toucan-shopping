package com.toucan.shopping.user.redis;

public class UserCenterRegistRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001002";





    /**
     * 注册锁
     * @param mobile
     * @return
     */
    public static String getRegistLockKey(String mobile)
    {
        return appCode +"_user_regist_"+mobile+"_lock";
    }



}
