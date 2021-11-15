package com.toucan.shopping.modules.seller.redis;

/**
 * 商户店铺
 */
public class SellerShopKey {



    /**
     * 保存商户店铺锁
     * @param userMainId
     * @return
     */
    public static String getSaveLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:SHOP:SAVE:"+userMainId+"_LOCK";
    }


    /**
     * 编辑商户店铺锁
     * @param userMainId
     * @return
     */
    public static String getUpdateLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:SHOP:UPDATE:"+userMainId+"_LOCK";
    }





    /**
     * 店铺缓存
     * @param userMainId
     * @return
     */
    public static String getShopCacheKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:SHOP:CACHE:"+userMainId+"_SHOP";
    }



}
