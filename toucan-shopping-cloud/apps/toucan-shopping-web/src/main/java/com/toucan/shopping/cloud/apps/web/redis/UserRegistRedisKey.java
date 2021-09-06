package com.toucan.shopping.cloud.apps.web.redis;

public class UserRegistRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001001";

    //验证码注册类型
    public static String smsRegistType="regist";



    /**
     * 验证码缓存
     * @param mobile
     * @return
     */
    public static String getVerifyCodeKey(String mobile)
    {
        return "TOUCAN_SHOPPING_WEB:"+appCode +"_"+smsRegistType+"_verifycode_"+mobile;
    }


    /**
     * 验证码锁键
     * @param mobile
     * @return
     */
    public static String getVerifyCodeLockKey(String mobile)
    {
        return "TOUCAN_SHOPPING_WEB:"+appCode +"_"+smsRegistType+"_verifycode_"+mobile+"_lock";
    }


    /**
     * 注册锁键
     * @param mobile
     * @return
     */
    public static String getRegistLockKey(String mobile)
    {
        return "TOUCAN_SHOPPING_WEB:"+appCode +"_"+smsRegistType+"_"+mobile+"_lock";
    }





}
