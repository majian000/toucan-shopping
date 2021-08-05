package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.seller.entity.SellerShop;

import java.util.List;

/**
 * 卖家店铺服务
 * @author majian
 * @date 2021-8-5 17:04:21
 */
public interface SellerShopService {


    int save(SellerShop entity);

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
    int update(SellerShop entity);


    List<SellerShop> findListByEntity(SellerShop query);

}
