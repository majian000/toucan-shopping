package com.toucan.shopping.modules.product.redis;

/**
 * 商品审核SKU Redis相关
 * @author majian
 */
public class ProductSkuRedisKey {

    public static String getPreviewSkuKey(String productApproveSkuId)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:PRODUCT:SKU:PREVIEW:"+productApproveSkuId;
    }
}