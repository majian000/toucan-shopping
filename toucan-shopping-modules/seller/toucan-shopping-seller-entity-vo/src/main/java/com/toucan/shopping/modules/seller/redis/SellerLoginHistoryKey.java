package com.toucan.shopping.modules.seller.redis;

/**
 * 卖家登录历史
 */
public class SellerLoginHistoryKey {



    /**
     * 保存锁
     * @param userMainId
     * @return
     */
    public static String getSaveLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:SELLER:LOGIN_HISTORY:SHOP:SAVE:"+userMainId+"_LOCK";
    }



}
