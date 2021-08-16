package com.toucan.shopping.cloud.apps.seller.web.redis;

public class ShopRegistRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001001";


    /**
     * 注册锁键
     * @param userMainId
     * @return
     */
    public static String getRegistLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_SELLER_WEB:SHOP:LOCK:SHOP_REGIST_"+userMainId+"_LOCK";
    }
}
