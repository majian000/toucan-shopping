package com.toucan.shopping.modules.product.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.product.entity.ShopProduct;
import com.toucan.shopping.modules.product.page.ShopProductPageInfo;
import com.toucan.shopping.modules.product.vo.ShopProductVO;

import java.util.List;

public interface ShopProductService {

    List<ShopProduct> queryAllList(ShopProduct queryModel);

    int save(ShopProduct entity);

    int deleteById(Long id);


    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<ShopProductVO> queryListPage(ShopProductPageInfo queryPageInfo);

}
