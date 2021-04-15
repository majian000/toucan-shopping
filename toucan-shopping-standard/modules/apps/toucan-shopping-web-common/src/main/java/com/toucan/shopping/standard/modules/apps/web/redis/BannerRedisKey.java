package com.toucan.shopping.standard.modules.apps.web.redis;

public class BannerRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001001";


    public static String getIndexBanner(String areaCode)
    {
        return appCode+"_INDEX_"+areaCode+"_BANNER";
    }

}
