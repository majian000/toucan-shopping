package com.toucan.shopping.cloud.apps.seller.web.redis;

public class ShopCategoryRedisKey {



    /**
     * 保存锁键
     * @param userMainId
     * @return
     */
    public static String getSaveLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_SELLER_WEB:SHOP:LOCK:SHOP_REGIST_"+userMainId+"_LOCK";
    }


    /**
     * 编辑锁键
     * @param userMainId
     * @return
     */
    public static String getUpdateLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_SELLER_WEB:SHOP:LOCK:SHOP_EDIT_"+userMainId+"_LOCK";
    }

}
