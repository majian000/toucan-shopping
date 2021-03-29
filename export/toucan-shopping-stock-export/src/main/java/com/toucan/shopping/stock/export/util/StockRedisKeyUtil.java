package com.toucan.shopping.stock.export.util;

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
