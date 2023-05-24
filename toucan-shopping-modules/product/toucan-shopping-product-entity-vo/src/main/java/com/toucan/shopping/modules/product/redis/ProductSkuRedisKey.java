package com.toucan.shopping.modules.product.redis;

/**
 * 商品SKU Redis相关
 * @author majian
 */
public class ProductSkuRedisKey {

    public static String getPreviewSkuKeyBySkuId(String id)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:PRODUCT:SKU:PREVIEW:ID:"+id;
    }

}
