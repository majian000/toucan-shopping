package com.toucan.shopping.modules.sller.service;


import com.toucan.shopping.modules.sller.entity.SllerShopCategory;

import java.util.List;

/**
 * 商户店铺类别服务
 * @author majian
 * @date 2021-8-5 17:04:21
 */
public interface SllerShopCategoryService {

    int save(SllerShopCategory entity);

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
    int update(SllerShopCategory entity);


    List<SllerShopCategory> findListByEntity(SllerShopCategory query);
}
