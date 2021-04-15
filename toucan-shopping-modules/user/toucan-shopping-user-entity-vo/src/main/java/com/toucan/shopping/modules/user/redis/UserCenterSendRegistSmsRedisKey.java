package com.toucan.shopping.modules.user.redis;

public class UserCenterSendRegistSmsRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001002";





    /**
     * 发送注册验证码锁
     * @param mobile
     * @return
     */
    public static String getSendRegistVerifyCodeLockKey(String mobile)
    {
        return appCode +"_send_regist__verifycode_"+mobile+"_lock";
    }


}
