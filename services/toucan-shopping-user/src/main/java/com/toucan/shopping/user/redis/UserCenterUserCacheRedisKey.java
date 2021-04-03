package com.toucan.shopping.user.redis;

public class UserCenterUserCacheRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001002";



    /**
     * 用户手机号缓存
     * @param mobilePhone
     * @return
     */
    public static String getUserCacheMobilePhoneKey(String mobilePhone)
    {
        return appCode +"_user_cache_mobile_phone_"+mobilePhone;
    }



    /**
     * 用户邮箱缓存
     * @param email
     * @return
     */
    public static String getUserCacheEmailKey(String email)
    {
        return appCode +"_user_cache_email_"+email;
    }

}
