package com.toucan.shopping.cloud.apps.web.redis;

public class UserBindEmailRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001001";

    //验证码注册类型
    public static String smsRegistType="bind_email";





    /**
     * 验证码缓存(邮箱)
     * @param userId
     * @return
     */
    public static String getEmailVerifyCodeKey(String userId)
    {
        return "TOUCAN_SHOPPING_WEB:VCODE:EMAIL:"+appCode +"_"+smsRegistType+"_verifycode_email_"+userId;
    }


    /**
     * 绑定邮箱键
     * @param userId
     * @return
     */
    public static String getBindEmailKey(String userId)
    {
        return "TOUCAN_SHOPPING_WEB:APPS:SHOPPING_WEB:BINDEMAIL:LOCK:"+appCode +"_"+userId;
    }


}
