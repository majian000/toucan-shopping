package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.Brand;
import com.toucan.shopping.modules.product.entity.BrandCategory;
import com.toucan.shopping.modules.product.page.BrandCategoryPageInfo;
import com.toucan.shopping.modules.product.vo.BrandCategoryVO;

import java.util.List;

public interface BrandCategoryService {

    List<BrandCategory> queryAllList(BrandCategory queryModel);

    List<BrandCategory> queryList(BrandCategory queryModel);

    int save(BrandCategory entity);

    List<BrandCategoryVO> queryListByBrandIds(List<Long> brandIds);

    PageInfo<BrandCategoryVO> queryListPage(BrandCategoryPageInfo queryPageInfo);


    /**
     * 返回所有品牌ID
     * @param categoryIds
     * @return
     */
    List<Long> queryBrandIdListByCategoryId(List<Long> categoryIds);
}
