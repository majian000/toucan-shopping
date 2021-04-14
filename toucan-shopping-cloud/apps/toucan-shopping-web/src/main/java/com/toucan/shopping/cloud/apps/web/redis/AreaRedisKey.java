package com.toucan.shopping.cloud.apps.web.redis;

public class AreaRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001001";


    /**
     * 登录键
     * @return
     */
    public static String getAreaKey()
    {
        return appCode +"_AREA_INDEX";
    }

}
