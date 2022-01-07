package com.toucan.shopping.cloud.apps.admin.auth.web.redis;

/**
 * 验证码 redis key管理
 */
public class VerifyCodeRedisKey {

    public static String getVerifyCodeKey(String appCode,String vcodeKey)
    {
        return "TOUCAN_ADMIN_AUTH:VERIFY_CODE:"+appCode+"_vcode_"+vcodeKey;
    }
}
