package com.toucan.shopping.area.service;



import com.toucan.shopping.area.entity.Banner;
import com.toucan.shopping.area.entity.BannerArea;

import java.util.List;

public interface BannerAreaService {

    List<BannerArea> queryList(BannerArea bannerArea);


    /**
     * 保存实体
     * @param entity
     * @return
     */
    int save(BannerArea entity);


    int deleteByBannerId(Long bannerId);
}
