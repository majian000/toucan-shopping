package com.toucan.shopping.modules.product.redis;


/**
 * 店铺商品锁
 * @author majian
 */
public class ShopProductRedisLockKey {


    public static String getResaveProductLockKey(String shopProductId)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:PRODUCT:CHANGE:"+shopProductId;
    }
}
