package com.toucan.shopping.modules.user.redis;

/**
 * 用户实名相关
 */
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
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:INDEX:USER_TRUE_NAME:SAVE:"+userMainId+"_LOCK";
    }



    /**
     * 保存用户实名审核锁
     * @param userMainId
     * @return
     */
    public static String getSaveApproveLockKeyForService(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:SERVICE:INDEX:USER_TRUE_NAME:SAVE:"+userMainId+"_LOCK";
    }




    /**
     * 修改用户实名审核锁
     * @param userMainId
     * @return
     */
    public static String getUpdateApproveLockKeyForService(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:USER_CENTER:LOCK:SERVICE:INDEX:USER_TRUE_NAME:UPDATE:"+userMainId+"_LOCK";
    }

}
