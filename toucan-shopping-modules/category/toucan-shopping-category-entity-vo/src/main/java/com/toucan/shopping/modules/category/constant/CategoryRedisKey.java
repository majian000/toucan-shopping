package com.toucan.shopping.modules.category.constant;

public class CategoryRedisKey {

    //默认值,在启动的时候会被替换成配置文件的blackBird.appCode
    public static String appCode = "10001001";


    /**
     * 门户端首页
     * @return
     */
    public static String getWebIndexKey()
    {
        return appCode+":WEB:INDEX:CATEGORY";
    }

}
