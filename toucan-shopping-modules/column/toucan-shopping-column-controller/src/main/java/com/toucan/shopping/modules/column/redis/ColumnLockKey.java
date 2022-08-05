package com.toucan.shopping.modules.column.redis;

/**
 * 栏目类型
 */
public class ColumnLockKey {


    /**
     * 保存栏目类型
     * @param key
     * @return
     */
    public static String getSaveLockKey(String key)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:COLUMN:SAVE:"+key+"_LOCK";
    }


}
