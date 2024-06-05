package com.toucan.shopping.modules.content.redis;

/**
 * 文章
 */
public class ArticleLockKey {


    /**
     * 文章
     * @param key
     * @return
     */
    public static String getSaveLockKey(String key)
    {
        return "TOUCAN_SHOPPING_WEB:SERVICES:ARTICLE:SAVE:"+key+"_LOCK";
    }


}
