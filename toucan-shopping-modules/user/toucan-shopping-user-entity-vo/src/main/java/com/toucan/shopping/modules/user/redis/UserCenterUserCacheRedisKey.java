package com.toucan.shopping.modules.user.redis;

/**
 * 用户缓存
 */
public class UserCenterUserCacheRedisKey {




    /**
     * 用户手机号缓存
     * @param mobilePhone
     * @return
     */
    public static String getUserCacheMobilePhoneKey(String mobilePhone)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:USER_CACHE_MOBILE_PHONE_"+mobilePhone;
    }



    /**
     * 用户邮箱缓存
     * @param email
     * @return
     */
    public static String getUserCacheEmailKey(String email)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:USER_CACHE_EMAIL_"+email;
    }

}
