package com.toucan.shopping.modules.column.redis;

/**
 * 栏目类型
 */
public class ColumnTypeLockKey {


    /**
     * 保存栏目类型
     */
    public static String getSaveLockKey(String key) {
        return "TOUCAN_SHOPPING_WEB:SERVICES:COLUMN:COLUMN_TYPE:SAVE:" + key + "_LOCK";
    }

    /**
     * 修改栏目类型
     */
    public static String getUpdateLockKey(String key) {
        return "TOUCAN_SHOPPING_WEB:SERVICES:COLUMN:COLUMN_TYPE:UPDATE:" + key + "_LOCK";
    }

}
