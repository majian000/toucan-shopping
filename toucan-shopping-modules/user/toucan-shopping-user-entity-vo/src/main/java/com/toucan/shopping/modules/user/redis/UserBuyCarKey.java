package com.toucan.shopping.modules.user.redis;

/**
 * 购物车
 */
public class UserBuyCarKey {






    /**
     * 添加商品到购物车
     * @param userMainId
     * @return
     */
    public static String getSaveLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:INDEX:USER_BUY_CAR:SAVE:"+userMainId+"_LOCK";
    }


}
