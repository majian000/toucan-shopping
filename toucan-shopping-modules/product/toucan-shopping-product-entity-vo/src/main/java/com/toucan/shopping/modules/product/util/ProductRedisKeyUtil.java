package com.toucan.shopping.modules.product.util;

public class ProductRedisKeyUtil {


    //商品SKU 180秒过期
    public static final int MAX_PRODUCT_SKU_SECONDS=180;


    /**
     * 商品秒杀活动状态
     * @param appCode
     * @param skuId
     * @return
     */
    public static String getProductActivityKey(String appCode,String skuId)
    {
        return appCode+"_product_"+skuId+"_activity";
    }

    /**
     * 秒杀活动全局锁
     * @param appCode
     * @param skuId
     * @return
     */
    public static String getGlobalSecondKillKey(String appCode,String skuId)
    {
        return  appCode+"_sk_service_"+skuId;
    }



    /**
     * 商品购买锁
     * @param appCode
     * @param skuUuid
     * @return
     */
    public static String getProductBuyKey(String appCode,String skuUuid)
    {
        return  appCode+"_product_buy_service_"+skuUuid;
    }


    /**
     * 商品缓存key
     * @param appCode
     * @param skuId
     * @return
     */
    public static String getProductKey(String appCode,String skuId)
    {
        return  appCode+"_product_"+skuId;
    }



    /**
     * 商品缓存key
     * @param appCode
     * @param uuid
     * @return
     */
    public static String getProductKeyByUuid(String appCode,String uuid)
    {
        return  appCode+"_product_uuid_"+uuid;
    }

}
