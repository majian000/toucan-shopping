package com.toucan.shopping.modules.user.redis;

public class UserCenterRegistRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001002";





    /**
     * 注册锁
     * @param mobile
     * @return
     */
    public static String getRegistLockKey(String mobile)
    {
        return appCode +"_user_regist_"+mobile+"_lock";
    }



    /**
     * 修改详情锁
     * @param mobile
     * @return
     */
    public static String getUpdateDetailLockKey(String mobile)
    {
        return appCode +"_user_update_detail_"+mobile+"_lock";
    }
    /**
     * 重置密码锁
     * @param userMainId
     * @return
     */
    public static String getResetPasswordLockKey(String userMainId)
    {
        return appCode +"_user_reset_password_"+userMainId+"_lock";
    }


}
