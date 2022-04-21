package com.toucan.shopping.modules.product.redis;

/**
 * 发布商品 redis锁
 */
public class ProductApproveRedisLockKey {

    public static String getSaveProductLockKey(String shopId)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:PRODUCT:APPROVE:SAVE:"+shopId;
    }


    public static String getResaveProductLockKey(String shopId)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:PRODUCT:APPROVE:RESAVE:"+shopId;
    }
    /**
     * 商品审核通过
     * @param productApproveId
     * @return
     */
    public static String getProductApprovePassLockKey(String productApproveId)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:PRODUCT:APPROVE:pass:"+productApproveId;
    }
}
