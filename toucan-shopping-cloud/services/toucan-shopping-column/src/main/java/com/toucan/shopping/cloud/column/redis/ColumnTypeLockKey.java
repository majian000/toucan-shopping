package com.toucan.shopping.cloud.column.redis;

/**
 * 栏目类型
 */
public class ColumnTypeLockKey {






    /**
     * 保存栏目类型
     * @param key
     * @return
     */
    public static String getSaveLockKey(String key)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:COLUMN:COLUMN_TYPE:SAVE:"+key+"_LOCK";
    }


}
