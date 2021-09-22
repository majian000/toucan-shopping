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
     * 刷新简洁版类别树
     * @param categoryVOS
     */
    void flushWMiniTree(List<CategoryVO> categoryVOS);

    /**
     * 查询商城首页缓存
     * @return
     */
    List<CategoryVO> queryWebIndexCache();


    /**
     * 查询简洁版的类别树(只有id,name,children)
     * @return
     */
    List<CategoryVO> queryMiniTree();


    /**
     * 查询商城首页缓存
     * @return
     */
    boolean clearWebIndexCache();

}

