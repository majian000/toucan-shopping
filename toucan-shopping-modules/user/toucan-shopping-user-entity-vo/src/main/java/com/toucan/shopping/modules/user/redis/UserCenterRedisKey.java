package com.toucan.shopping.modules.user.redis;

/**
 * 用户相关
 */
public class UserCenterRedisKey {







    /**
     * 修改详情锁
     * @param userMainId
     * @return
     */
    public static String getEditInfoLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:USER_EDIT_INFO:"+userMainId+"_LOCK";
    }


}
