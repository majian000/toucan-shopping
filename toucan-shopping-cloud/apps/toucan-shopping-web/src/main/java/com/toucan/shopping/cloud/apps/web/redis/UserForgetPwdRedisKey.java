package com.toucan.shopping.cloud.apps.web.redis;

public class UserForgetPwdRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001001";

    //验证码注册类型
    public static String smsRegistType="forget_pwd";



    /**
     * 验证码缓存(手机)
     * @param userId
     * @return
     */
    public static String getMobileVerifyCodeKey(String userId)
    {
        return "TOUCAN_SHOPPING_WEB:VCODE:MOBILE:"+appCode +"_"+smsRegistType+"_verifycode_mobile_"+userId;
    }




    /**
     * 验证码缓存(邮箱)
     * @param userId
     * @return
     */
    public static String getEmailVerifyCodeKey(String userId)
    {
        return "TOUCAN_SHOPPING_WEB:VCODE:EMAIL:"+appCode +"_"+smsRegistType+"_verifycode_email_"+userId;
    }



}
