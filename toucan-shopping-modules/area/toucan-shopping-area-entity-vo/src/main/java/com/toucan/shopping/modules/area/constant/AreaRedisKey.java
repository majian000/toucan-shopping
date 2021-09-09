package com.toucan.shopping.modules.area.constant;

public class AreaRedisKey {

    /**
     * 返回地区key前缀
     * @return
     */
    public static String getCachePrefixKey()
    {
        return "TOUCAN_SHOPPING_AREA:CACHE";
    }

    /**
     * 省键
     * @return
     */
    public static String getProvinceCacheKey()
    {
        return getCachePrefixKey()+":PROVINCE:PROVINCE_LIST";
    }

    /**
     * 地市键
     * @return
     */
    public static String getCityCacheKey(String prefix)
    {
        return getCachePrefixKey()+":CITY:PARENT_"+prefix+"_CITY_LIST";
    }

    /**
     * 区县键
     * @return
     */
    public static String getAreaCacheKey(String cityPrefix)
    {
        return getCachePrefixKey()+":AREA:PARENT_"+cityPrefix+"_AREA_LIST";
    }

    /**
     * 完全缓存
     * @return
     */
    public static String getFullAreaCacheKey()
    {
        return getCachePrefixKey()+":FULL_CACHE:PARENT_AREA_LIST";
    }

}
