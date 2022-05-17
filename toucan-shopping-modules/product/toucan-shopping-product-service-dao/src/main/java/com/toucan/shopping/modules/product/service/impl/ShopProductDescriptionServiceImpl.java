package com.toucan.shopping.modules.product.service.impl;

import com.toucan.shopping.modules.product.entity.ShopProductDescription;
import com.toucan.shopping.modules.product.mapper.ShopProductDescriptionMapper;
import com.toucan.shopping.modules.product.service.ShopProductDescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShopProductDescriptionServiceImpl implements ShopProductDescriptionService {

    @Autowired
    private ShopProductDescriptionMapper shopProductDescriptionMapper;

    @Override
    public int save(ShopProductDescription entity) {
        return shopProductDescriptionMapper.insert(entity);
    }

    @Override
    public int deleteByShopProductApproveId(Long productApproveId) {
        return shopProductDescriptionMapper.deleteByShopProductId(productApproveId);
    }

    @Override
    public ShopProductDescription queryByShopProductId(Long productApproveId) {
        return shopProductDescriptionMapper.queryByShopProductId(productApproveId);
    }
}
