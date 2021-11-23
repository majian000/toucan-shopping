package com.toucan.shopping.cloud.apps.seller.web.redis;

public class ShopRegistRedisKey {



    /**
     * 注册锁键
     * @param userMainId
     * @return
     */
    public static String getRegistLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_SELLER_WEB:SHOP:LOCK:SHOP_REGIST_"+userMainId+"_LOCK";
    }


    /**
     * 编辑锁键
     * @param userMainId
     * @return
     */
    public static String getEditLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_SELLER_WEB:SHOP:LOCK:SHOP_EDIT_"+userMainId+"_LOCK";
    }


    public static String getVerifyCodeKey(String phone)
    {
        return "TOUCAN_SHOPPING_SELLER_WEB:SHOP:SHOP_REGIST_VERIFY_CODE_"+phone;
    }

}
