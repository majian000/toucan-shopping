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
        return "TOUCAN_SHOPPING_WEB:INDEX:CATEGORY";
    }


    /**
     * 简洁版类别树
     * @return
     */
    public static String getMiniTreeKey()
    {
        return "TOUCAN_SHOPPING_WEB:INDEX:CATEGORY:MINI";
    }

    /**
     * 简洁版导航条类别树
     * @return
     */
    public static String getNavigationMiniTreeKey()
    {
        return "TOUCAN_SHOPPING_WEB:INDEX:CATEGORY:NAVIGATION:MINI";
    }

    /**
     * 完整分类树结构
     * @return
     */
    public static String getFullTreeKey()
    {
        return "TOUCAN_SHOPPING_WEB:INDEX:CATEGORY:FULL:TREE";
    }

}
