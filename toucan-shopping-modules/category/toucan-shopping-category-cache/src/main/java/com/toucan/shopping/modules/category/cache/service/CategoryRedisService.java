package com.toucan.shopping.modules.category.cache.service;


import com.toucan.shopping.modules.category.vo.CategoryVO;

import java.util.List;

public interface CategoryRedisService {


    /**
     * 刷新缓存
     * @param categoryVOS
     */
    void flushWebIndexCaches(List<CategoryVO> categoryVOS);

    /**
     * 查询商城首页缓存
     * @return
     */
    List<CategoryVO> queryWebIndexCache();

    /**
     * 查询商城首页缓存
     * @return
     */
    boolean clearWebIndexCache();

}

