package com.toucan.shopping.modules.product.redis;

/**
 * 商品审核SKU Redis相关
 * @author majian
 */
public class ProductApproveSkuRedisKey {

    public static String getPreviewSkuKey(String productApproveSkuId)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:PRODUCT:APPROVE:SKU:PREVIEW:"+productApproveSkuId;
    }
}
