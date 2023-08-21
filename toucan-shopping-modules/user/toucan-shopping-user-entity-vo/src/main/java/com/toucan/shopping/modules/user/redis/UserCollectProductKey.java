package com.toucan.shopping.modules.user.redis;

/**
 * 用户收藏商品
 */
public class UserCollectProductKey {


    /**
     * 保存收货人信息
     * @param userMainId
     * @return
     */
    public static String getCollectKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:INDEX:USER_COLLECT_PRODUCT:COLLECT:"+userMainId+"_LOCK";
    }

}
