package com.toucan.shopping.cloud.apps.seller.web.redis;

/**
 * 验证码 redis key管理
 */
public class VerifyCodeRedisKey {

    public static String getVerifyCodeKey(String appCode,String vcodeKey)
    {
        return "TOUCAN_SHOPPING_SELLER_WEB:VERIFY_CODE:"+appCode+"_vcode_"+vcodeKey;
    }



    public static String getLoginFaildVerifyCodeKey(String appCode,String vcodeKey)
    {
        return "TOUCAN_SHOPPING_SELLER_WEB:LOGIN_FAILD_VERIFY_CODE:"+appCode+"_vcode_"+vcodeKey;
    }

}
