package com.toucan.shopping.cloud.apps.web.redis;

public class UserEditInfoRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001001";


    /**
     * 编辑信息锁键
     * @param userMainId
     * @return
     */
    public static String getEditInfoLockKey(String userMainId)
    {
        return "TOUCAN_SHOPPING_WEB:"+appCode +"_EDIT:INFO_"+userMainId+"_lock";
    }


}
