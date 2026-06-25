package com.toucan.shopping.modules.column.redis;

/**
 * 热门商品
 */
public class HotProductLockKey {


    /**
     * 保存热门商品
     * @param key
     * @return
     */
    public static String getSaveLockKey(String key)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:HOT:PRODUCT:SAVE:"+key+"_LOCK";
    }


    /**
     * 修改热门商品
     * @param key
     * @return
     */
    public static String getUpdateLockKey(String key)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:HOT:PRODUCT:UPDATE:"+key+"_LOCK";
    }
}
