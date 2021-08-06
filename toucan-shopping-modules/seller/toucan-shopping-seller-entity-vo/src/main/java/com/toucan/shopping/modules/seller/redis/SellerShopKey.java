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


}
