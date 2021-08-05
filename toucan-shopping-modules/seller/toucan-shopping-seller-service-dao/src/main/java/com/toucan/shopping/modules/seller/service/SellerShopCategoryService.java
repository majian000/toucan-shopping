package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.seller.entity.SellerShopCategory;

import java.util.List;

/**
 * 商户店铺类别服务
 * @author majian
 * @date 2021-8-5 17:04:21
 */
public interface SellerShopCategoryService {

    int save(SellerShopCategory entity);

    /**
     * 根据ID删除
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 修改
     * @param entity
     * @return
     */
    int update(SellerShopCategory entity);


    List<SellerShopCategory> findListByEntity(SellerShopCategory query);
}
