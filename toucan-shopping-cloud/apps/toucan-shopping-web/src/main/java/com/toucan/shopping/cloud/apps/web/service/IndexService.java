package com.toucan.shopping.cloud.apps.web.service;

import com.toucan.shopping.modules.column.vo.PcIndexColumnVO;
import com.toucan.shopping.modules.content.vo.BannerVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;

import java.util.List;

/**
 * 首页服务类(查询类别、地区列表等)
 */
public interface IndexService {


    /**
     * 查询首页轮播图列表
     * @return
     */
    List<BannerVO> queryBanners();

    /**
     * 查询首页类别列表
     * @return
     */

    List<CategoryVO> queryCategorys();

    /**
     * 查询首页类别列表
     * @return
     */

    List<CategoryVO> queryNavigationCategorys();

    /**
     * 查询首页商品推荐栏目
     * @return
     */
    List<PcIndexColumnVO> queryColumns();

}
