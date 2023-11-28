package com.toucan.shopping.modules.seller.service;


import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.ShopBanner;
import com.toucan.shopping.modules.seller.entity.ShopImage;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.page.ShopImagePageInfo;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import com.toucan.shopping.modules.seller.vo.ShopImageVO;

import java.util.List;

public interface ShopImageService {


    ShopImage findById(Long id);

    /**
     * 查询列表页
     * @param pageInfo
     * @return
     */
    PageInfo<ShopImageVO> queryListPage(ShopImagePageInfo pageInfo);

}
