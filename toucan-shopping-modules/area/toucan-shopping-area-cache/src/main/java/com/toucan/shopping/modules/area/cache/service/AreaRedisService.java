package com.toucan.shopping.modules.area.cache.service;

import com.toucan.shopping.modules.area.vo.AreaVO;
import com.toucan.shopping.modules.area.vo.BannerVO;

import java.util.List;

public interface AreaRedisService {

    /**
     * 刷新省缓存
     * @param areaVOS
     */
    void flushProvinceCache(List<AreaVO> areaVOS);

    /**
     * 刷新地市缓存
     * @param key
     * @param areaVOS
     */
    void flushCityCache(String key,List<AreaVO> areaVOS);

    /**
     * 刷新区县缓存
     * @param key
     * @param areaVOS
     */
    void flushAreaCache(String key,List<AreaVO> areaVOS);

    /**
     * 清空缓存
     */
    void clearAreaCache();
}

