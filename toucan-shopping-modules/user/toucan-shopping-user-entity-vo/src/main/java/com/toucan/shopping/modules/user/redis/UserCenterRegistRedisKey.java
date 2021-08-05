package com.toucan.shopping.modules.user.redis;

/**
 * 用户注册相关
 */
public class UserCenterRegistRedisKey {






    /**
     * 注册锁
     * @param mobile
     * @return
     */
    public static String getRegistLockKey(String mobile)
    {
        return "TOUCAN_SHOPPING_WEB:USER_REGIST_"+mobile+"_LOCK";
    }



    /**
     * 修改详情锁
     * @param mobile
     * @return
     */
    public static String getUpdateDetailLockKey(String mobile)
    {
        return "TOUCAN_SHOPPING_WEB:USER_UPDATE_DETAIL_"+mobile+"_LOCK";
    }
    /**
     * 重置密码锁
     * @param userMainId
     * @return
     */
    public static String getResetPasswordLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_RESET_PASSWORD_"+userMainId+"_LOCK";
    }


}
