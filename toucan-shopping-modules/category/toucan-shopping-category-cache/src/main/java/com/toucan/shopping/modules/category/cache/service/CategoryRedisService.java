package com.toucan.shopping.modules.category.cache.service;


import com.toucan.shopping.modules.category.entity.Category;
import com.toucan.shopping.modules.category.vo.CategoryTreeVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;

import java.util.List;

public interface CategoryRedisService {

    /**
     * 清空所有缓存
     */
    void clearCaches();

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
     * 刷新导航条树
     * @param categoryVOS
     */
    void flushNavigationMiniTree(List<CategoryVO> categoryVOS);

    /**
     * 查询商城首页缓存
     * @return
     */
    List<CategoryVO> queryWebIndexCache();


    /**
     * 查询分类导航条缓存
     * @return
     */
    List<CategoryVO> queryWebNavigationCache();
    /**
     * 查询简洁版的类别树(只有id,name,children)
     * @return
     */
    List<CategoryVO> queryMiniTree();


    /**
     * 清空商城首页缓存
     * @return
     */
    boolean clearWebIndexCache();

    /**
     * 清空简洁版的类别树(只有id,name,children)
     * @return
     */
    boolean clearMiniTreeCache();


    /**
     * 清空分类导航条缓存
     * @return
     */
    boolean clearWebNavigationCache();

    /**
     * 查询完整树结构
     * @return
     */
    List<CategoryTreeVO> queryFullCategoryTree();


    /**
     * 清空分类完整树
     * @return
     */
    boolean clearFullCategoryTree();


    /**
     * 刷新缓存
     * @param categoryVOS
     */
    void addFullCategoryCache(List<CategoryTreeVO> categoryVOS);
}

