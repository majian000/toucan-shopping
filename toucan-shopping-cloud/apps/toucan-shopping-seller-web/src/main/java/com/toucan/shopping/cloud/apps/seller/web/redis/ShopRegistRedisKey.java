package com.toucan.shopping.cloud.apps.seller.web.redis;

public class ShopRegistRedisKey {



    /**
     * 注册锁键
     * @param userMainId
     * @return
     */
    public static String getRegistLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_SELLER_WEB:SHOP:LOCK:SHOP_REGIST_"+userMainId+"_LOCK";
    }
}
