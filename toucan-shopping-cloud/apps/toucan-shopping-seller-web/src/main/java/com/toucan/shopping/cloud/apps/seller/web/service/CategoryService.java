package com.toucan.shopping.cloud.apps.seller.web.service;

import com.toucan.shopping.modules.area.vo.BannerVO;
import com.toucan.shopping.modules.category.vo.CategoryVO;

import java.util.List;

/**
 * 类别服务类
 */
public interface CategoryService {


    /**
     * 查询首页类别列表
     * @return
     */

    List<CategoryVO> queryCategorys();


}
