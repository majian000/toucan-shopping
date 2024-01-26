package com.toucan.shopping.modules.user.redis;

/**
 * 用户统计
 */
public class UserStatisticKey {


    /**
     * 用户总数
     * @return
     */
    public static String getUserTotalKey()
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:STATISTIC:TOTAL";
    }

}
