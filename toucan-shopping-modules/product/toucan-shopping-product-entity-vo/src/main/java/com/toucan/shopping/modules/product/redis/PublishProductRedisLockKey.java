package com.toucan.shopping.modules.product.redis;

/**
 * 发布商品 redis锁
 */
public class PublishProductRedisLockKey {

    public static String getPublishProductLockKey(String shopId)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:PRODUCT:PUBLISH:PRODUCT:"+shopId;
    }
}
