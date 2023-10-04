package com.toucan.shopping.modules.seller.redis;

/**
 * 店铺轮播图
 */
public class ShopBannerKey {



    /**
     * 删除店铺轮播图
     * @param shopId
     * @return
     */
    public static String getDeleteLockKey(String shopId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:SHOP:CATEGORY:BANNER:"+shopId+"_LOCK";
    }


}
