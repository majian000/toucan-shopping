package com.toucan.shopping.modules.column.redis;

/**
 * 栏目
 */
public class ColumnLockKey {


    /**
     * 保存栏目
     * @param key
     * @return
     */
    public static String getSaveLockKey(String key)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:COLUMN:SAVE:"+key+"_LOCK";
    }


    /**
     * 修改栏目
     * @param key
     * @return
     */
    public static String getUpdateLockKey(String key)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:COLUMN:UPDATE:"+key+"_LOCK";
    }
}
