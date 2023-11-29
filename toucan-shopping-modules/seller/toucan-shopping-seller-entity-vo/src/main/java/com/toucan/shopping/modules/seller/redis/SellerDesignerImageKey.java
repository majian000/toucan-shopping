package com.toucan.shopping.modules.seller.redis;

/**
 * 店铺装修图片
 */
public class SellerDesignerImageKey {



    /**
     * 删除锁
     * @param shopId
     * @return
     */
    public static String getDeleteLockKey(String shopId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:DESIGNER:IMAGE:"+shopId+"_LOCK";
    }


}
