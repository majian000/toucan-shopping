package com.toucan.shopping.modules.product.redis;

/**
 * 保存商品SPU redis锁
 */
public class ProductSpuRedisLockKey {

    public static String getSaveProductSpuLockKey(String categoryId)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:PRODUCT:SPU:SAVE:"+categoryId;
    }
}
