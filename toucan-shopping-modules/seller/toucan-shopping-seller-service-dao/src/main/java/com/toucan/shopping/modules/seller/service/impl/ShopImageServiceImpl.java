package com.toucan.shopping.modules.seller.service.impl;

import com.toucan.shopping.modules.common.page.PageInfo;
import com.toucan.shopping.modules.seller.entity.ShopBanner;
import com.toucan.shopping.modules.seller.entity.ShopImage;
import com.toucan.shopping.modules.seller.mapper.ShopBannerMapper;
import com.toucan.shopping.modules.seller.mapper.ShopImageMapper;
import com.toucan.shopping.modules.seller.page.ShopBannerPageInfo;
import com.toucan.shopping.modules.seller.service.ShopBannerService;
import com.toucan.shopping.modules.seller.service.ShopImageService;
import com.toucan.shopping.modules.seller.vo.ShopBannerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopImageServiceImpl implements ShopImageService {

    @Autowired
    private ShopImageMapper shopImageMapper;


    @Override
    public ShopImage findById(Long id) {
        return shopImageMapper.findById(id);
    }

}
