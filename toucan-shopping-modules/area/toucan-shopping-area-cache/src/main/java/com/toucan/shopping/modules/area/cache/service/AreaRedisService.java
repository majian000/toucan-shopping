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
     * 查询所有省
     * @return
     */
    List<AreaVO> queryProvinceList();


    /**
     * 刷新地市缓存
     * @param key
     * @param areaVOS
     */
    void flushCityCache(String key,List<AreaVO> areaVOS);


    /**
     * 查询指定省下的所有地市
     * @return
     */
    List<AreaVO> queryCityListByProvinceCode(String provinceCode);

    /**
     * 刷新区县缓存
     * @param key
     * @param areaVOS
     */
    void flushAreaCache(String key,List<AreaVO> areaVOS);


    /**
     * 查询指定地市的所有区县
     * @return
     */
    List<AreaVO> queryAreaListByCityCode(String cityCode);
    /**
     * 清空缓存
     */
    void clearAreaCache();
}

