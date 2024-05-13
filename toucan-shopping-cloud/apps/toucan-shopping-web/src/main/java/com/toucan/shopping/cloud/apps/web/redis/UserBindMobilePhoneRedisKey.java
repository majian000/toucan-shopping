package com.toucan.shopping.cloud.apps.web.redis;

public class UserBindMobilePhoneRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001001";

    //验证码注册类型
    public static String smsRegistType="bind_email";





    /**
     * 验证码缓存(手机号)
     * @param userId
     * @return
     */
    public static String getMobilePhoneVerifyCodeKey(String userId)
    {
        return "TOUCAN_SHOPPING_WEB:VCODE:MOBILEPHONE:"+appCode +"_"+smsRegistType+"_verifycode_email_"+userId;
    }


    /**
     * 绑定手机号键
     * @param userId
     * @return
     */
    public static String getBindMobilePhoneLockKey(String userId)
    {
        return "TOUCAN_SHOPPING_WEB:APPS:SHOPPING_WEB:BINDMOBILEPHONE:LOCK:"+appCode +"_"+userId;
    }


}
