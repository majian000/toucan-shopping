package com.toucan.shopping.modules.stock.util;

public class StockRedisKeyUtil {


    /**
     * 返回商品库存数量
     * @param appCode
     * @param skuUuid
     * @return
     */
    public static String getStockKey(String appCode,String skuUuid)
    {
        return appCode+"_stock_"+skuUuid;
    }
}
