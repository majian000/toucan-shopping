package com.toucan.shopping.modules.user.redis;

public class UserCenterTrueNameApproveKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001002";





    /**
     * 保存用户实名审核锁
     * @param userMainId
     * @return
     */
    public static String getSaveApproveLockKey(String userMainId)
    {
        return appCode +"_user_true_name_save_"+userMainId+"_lock";
    }



}
