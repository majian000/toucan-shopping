package com.toucan.shopping.modules.user.redis;

/**
 * 用户注册发送短信
 */
public class UserCenterSendRegistSmsRedisKey {



    /**
     * 发送注册验证码锁
     * @param mobile
     * @return
     */
    public static String getSendRegistVerifyCodeLockKey(String mobile)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:SEND_REGIST__VERIFYCODE_"+mobile+"_LOCK";
    }


}
