package com.toucan.shopping.cloud.apps.h5.service;

import com.toucan.shopping.modules.category.vo.CategoryVO;
import com.toucan.shopping.modules.column.vo.HotProductVO;
import com.toucan.shopping.modules.content.vo.BannerVO;

import java.util.List;

/**
 * H5 首页服务（查询轮播图、分类、热门商品等）
 */
public interface IndexService {

    /** 查询首页轮播图 */
    List<BannerVO> queryBanners();

    /** 查询首页分类列表 */
    List<CategoryVO> queryCategorys();

    /** 查询分类导航列表 */
    List<CategoryVO> queryNavigationCategorys();

    /** 查询首页热门商品 */
    List<HotProductVO> queryHotProduces();
}
