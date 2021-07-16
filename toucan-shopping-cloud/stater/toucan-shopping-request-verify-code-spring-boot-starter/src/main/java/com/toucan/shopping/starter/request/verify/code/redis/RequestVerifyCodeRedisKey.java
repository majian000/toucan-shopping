package com.toucan.shopping.starter.request.verify.code.redis;

/**
 * 验证码 redis key管理
 */
public class RequestVerifyCodeRedisKey {

    public static String getRequestVerifyCodeKey(String appCode,String vcodeKey)
    {
        return "TOUCAN_SHOPPING_WEB:REQUEST_VERIFY_CODE:VERIFY_CODE:"+appCode+"_vcode_"+vcodeKey;
    }
}
