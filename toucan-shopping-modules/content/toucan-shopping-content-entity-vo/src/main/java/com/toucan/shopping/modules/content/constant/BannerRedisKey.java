package com.toucan.shopping.modules.content.constant;

public class BannerRedisKey {

    public static String appCode = "10001001";


    /**
     * 门户端首页
     * @return
     */
    public static String getWebIndexBanner()
    {
        return "TOUCAN_SHOPPING_WEB:INDEX:BANNER";
    }

}
