package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.seller.entity.SellerShopApproveRecord;

import java.util.List;

/**
 * 商户店铺审核服务
 * @author majian
 * @date 2021-8-5 17:04:21
 */
public interface SellerShopApproveRecordService {

    int save(SellerShopApproveRecord entity);

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
    int update(SellerShopApproveRecord entity);


    List<SellerShopApproveRecord> findListByEntity(SellerShopApproveRecord query);
}
