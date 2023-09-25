package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.ShopBanner;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;

import java.util.List;

public interface ShopBannerService {

    List<ShopBannerVO> queryList(ShopBannerVO banner);

    List<ShopBannerVO> queryIndexList(ShopBannerVO banner);


    /**
     * 保存实体
     * @param banner
     * @return
     */
    int save(ShopBanner banner);

    int deleteById(Long id);

    int update(ShopBanner banner);

    ShopBanner findById(Long id);

    /**
     * 查询列表页
     * @param appPageInfo
     * @return
     */
    PageInfo<ShopBannerVO> queryListPage(ShopBannerPageInfo appPageInfo);



}
