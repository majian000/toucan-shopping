package com.toucan.shopping.cloud.apps.web.redis;

/**
 * 验证码 redis key管理
 */
public class VerifyCodeRedisKey {

    public static String getVerifyCodeKey(String appCode,String vcodeKey)
    {
        return "TOUCAN_WEB:VERIFY_CODE:"+appCode+"_vcode_"+vcodeKey;
    }
}
