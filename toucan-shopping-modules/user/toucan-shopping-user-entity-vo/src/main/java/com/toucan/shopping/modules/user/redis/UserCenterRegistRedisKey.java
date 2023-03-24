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
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:USER_REGIST_"+mobile+"_LOCK";
    }



    /**
     * 修改详情锁
     * @param mobile
     * @return
     */
    public static String getUpdateDetailLockKey(String mobile)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:USER_UPDATE_DETAIL_"+mobile+"_LOCK";
    }
    /**
     * 重置密码锁
     * @param userMainId
     * @return
     */
    public static String getResetPasswordLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:USER_RESET_PASSWORD_"+userMainId+"_LOCK";
    }


    /**
     * 绑定邮箱锁
     * @param userId
     * @return
     */
    public static String getBindEmailLock(String userId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:USER_BIND_EMAIL_"+userId+"_LOCK";
    }

    /**
     * 绑定手机号锁
     * @param userId
     * @return
     */
    public static String getBindMobilePhoneLock(String userId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:USER_BIND_MOBILE_PHONE_"+userId+"_LOCK";
    }


    /**
     * 绑定用户名锁
     * @param userId
     * @return
     */
    public static String getBindUserNameLock(String userId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:USER_BIND_USERNAME_"+userId+"_LOCK";
    }
}
