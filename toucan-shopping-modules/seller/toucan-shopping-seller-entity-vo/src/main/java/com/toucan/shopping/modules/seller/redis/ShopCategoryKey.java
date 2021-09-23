package com.toucan.shopping.modules.seller.redis;

/**
 * 商户店铺类别
 */
public class ShopCategoryKey {



    /**
     * 保存商户店铺类别锁
     * @param userMainId
     * @return
     */
    public static String getSaveLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:SHOP:CATEGORY:SAVE:"+userMainId+"_LOCK";
    }


    /**
     * 编辑商户店铺类别锁
     * @param userMainId
     * @return
     */
    public static String getUpdateLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:SHOP:CATEGORY:UPDATE:"+userMainId+"_LOCK";
    }


    /**
     * 保存商户店铺类别缓存键
     * @param shopId
     * @return
     */
    public static String getCacheKey(Long shopId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:SHOP:CATEGORY:CACHE:SHOP_:"+shopId+"_CATEGORYS";
    }

}
