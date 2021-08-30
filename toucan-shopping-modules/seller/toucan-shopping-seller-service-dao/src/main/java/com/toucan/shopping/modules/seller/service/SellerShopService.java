package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.SellerShop;
import com.toucan.shopping.modules.seller.page.SellerShopPageInfo;

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


    /**
     * 修改图标
     * @param entity
     * @return
     */
    int updateLogo(SellerShop entity);

    List<SellerShop> findListByEntity(SellerShop query);


    /**
     * 查询列表页
     * @param queryPageInfo
     * @return
     */
    PageInfo<SellerShop> queryListPage(SellerShopPageInfo queryPageInfo);

}
