package com.toucan.shopping.standard.apps.web.redis;

public class UserLoginRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001001";

    //验证码登录类型
    public static String methodType ="login";




    /**
     * 验证码锁键
     * @param smsType
     * @param mobile
     * @return
     */
    public static String getVerifyCodeLockKey(String smsType,String mobile)
    {
        return appCode +"_"+smsType+"_"+mobile+"_lock";
    }


    /**
     * 登录锁键
     * @param username
     * @return
     */
    public static String getLoginLockKey(String username)
    {
        return appCode +"_"+ methodType +"_"+username+"_lock";
    }

    /**
     * 登录键
     * @param mobile
     * @return
     */
    public static String getLoginKey(String mobile)
    {
        return appCode +"_"+ methodType +"_"+mobile;
    }

}
