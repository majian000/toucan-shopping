package com.toucan.shopping.modules.area.constant;

public class BannerRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001001";


    /**
     * 门户端首页
     * @return
     */
    public static String getWebIndexBanner()
    {
        return appCode+":WEB:INDEX:BANNER";
    }

}
