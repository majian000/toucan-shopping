package com.toucan.shopping.modules.sller.service;


import com.toucan.shopping.modules.sller.entity.SllerShop;

import java.util.List;

/**
 * 卖家店铺服务
 * @author majian
 * @date 2021-8-5 17:04:21
 */
public interface SllerShopService {


    int save(SllerShop entity);

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
    int update(SllerShop entity);


    List<SllerShop> findListByEntity(SllerShop query);

}
