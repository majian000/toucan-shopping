package com.toucan.shopping.modules.message.redis;

/**
 * 消息类型
 */
public class MessageTypeKey {






    /**
     * 保存消息类型
     * @param key
     * @return
     */
    public static String getSaveLockKey(String key)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:MESSAGE:MESSAGE_TYPE:SAVE:"+key+"_LOCK";
    }


}
