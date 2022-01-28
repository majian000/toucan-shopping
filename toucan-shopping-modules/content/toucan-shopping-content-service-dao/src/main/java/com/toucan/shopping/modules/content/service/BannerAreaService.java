package com.toucan.shopping.modules.content.service;




import com.toucan.shopping.modules.content.entity.BannerArea;

import java.util.List;

public interface BannerAreaService {

    List<BannerArea> queryList(BannerArea bannerArea);


    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(BannerArea entity);

    int saves(BannerArea[] entitys);

    int deleteByBannerId(Long bannerId);
}
