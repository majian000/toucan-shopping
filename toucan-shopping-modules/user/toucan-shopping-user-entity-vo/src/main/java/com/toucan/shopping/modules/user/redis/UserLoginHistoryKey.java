package com.toucan.shopping.modules.user.redis;

/**
 * 用户登录历史
 */
public class UserLoginHistoryKey {



    /**
     * 保存锁
     * @param userMainId
     * @return
     */
    public static String getSaveLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:USER_LOGIN_HISTORY:SAVE:"+userMainId+"_LOCK";
    }


    /**
     * 编辑锁
     * @param userMainId
     * @return
     */
    public static String getUpdateLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:USER_LOGIN_HISTORY:UPDATE:"+userMainId+"_LOCK";
    }

}
