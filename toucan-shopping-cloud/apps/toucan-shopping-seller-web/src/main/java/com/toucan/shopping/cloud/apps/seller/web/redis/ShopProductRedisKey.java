package com.toucan.shopping.cloud.apps.seller.web.redis;

/**
 * 验证码 redis key管理
 */
public class ShopProductRedisKey {

    public static String getVerifyCodeKey(String appCode,String vcodeKey)
    {
        return "TOUCAN_SHOPPING_SELLER_WEB:PRODUCT:PUBLISH:"+appCode+"_vcode_"+vcodeKey;
    }



}
