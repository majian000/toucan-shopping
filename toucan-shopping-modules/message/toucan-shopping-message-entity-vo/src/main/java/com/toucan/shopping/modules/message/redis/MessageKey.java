package com.toucan.shopping.modules.message.redis;

/**
 * 消息
 */
public class MessageKey {






    /**
     * 保存消息
     * @param key
     * @return
     */
    public static String getSaveLockKey(String key)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:MESSAGE:MESSAGE:SAVE:"+key+"_LOCK";
    }



    /**
     * 保存消息
     * @param key
     * @return
     */
    public static String getUpdateLockKey(String key)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:MESSAGE:MESSAGE:UPDATE:"+key+"_LOCK";
    }


}
